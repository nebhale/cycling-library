/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.cyclinglibrary.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

@Component
final class GoogleMapsPointAugmenter implements PointAugmenter {

    private static final long DELAY_INCREMENT = 3 * 1000;

    private static final int MAX_URL_LENGTH = 2048;

    private static final String ELEVATION_URL = "https://maps.googleapis.com/maps/api/elevation/json?sensor=false&locations=";

    private final PolylineEncoder polylineEncoder;

    private final RestTemplate restTemplate;

    private final ScheduledExecutorService scheduledExecutorService;

    private final TaskRepository taskRepository;

    @Autowired
    GoogleMapsPointAugmenter(PolylineEncoder polylineEncoder, RestTemplate restTemplate, ScheduledExecutorService scheduledExecutorService,
        TaskRepository taskRepository) {
        this.polylineEncoder = polylineEncoder;
        this.restTemplate = restTemplate;
        this.scheduledExecutorService = scheduledExecutorService;
        this.taskRepository = taskRepository;
    }

    @Override
    public Task augmentPoints(Double[][] points, PointAugmenterCallback callback) {
        List<String> encodedPolylines = this.polylineEncoder.encode(MAX_URL_LENGTH - ELEVATION_URL.length(), points);
        Task task = this.taskRepository.create("Augmenting %d segments", encodedPolylines.size());

        this.scheduledExecutorService.execute(new PointsAugmentingRunnable(this.restTemplate, this.scheduledExecutorService, this.taskRepository,
            task, encodedPolylines, callback));

        return task;
    }

    private static final class PointsAugmentingRunnable implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        private final RestTemplate restTemplate;

        private final ScheduledExecutorService scheduledExecutorService;

        private final TaskRepository taskRepository;

        private final Task task;

        private final List<String> encodedPolylines;

        private final PointAugmenterCallback callback;

        private PointsAugmentingRunnable(RestTemplate restTemplate, ScheduledExecutorService scheduledExecutorService, TaskRepository taskRepository,
            Task task, List<String> encodedPolylines, PointAugmenterCallback callback) {
            this.restTemplate = restTemplate;
            this.scheduledExecutorService = scheduledExecutorService;
            this.taskRepository = taskRepository;
            this.task = task;
            this.encodedPolylines = encodedPolylines;
            this.callback = callback;
        }

        @Override
        public void run() {
            List<Future<List<Point>>> futures = startAugments();
            List<Point> points = new ArrayList<>();

            try {
                for (int i = 0; i < futures.size(); i++) {
                    points.addAll(futures.get(i).get());
                    this.taskRepository.update(this.task.getId(), Status.IN_PROGRESS, "Augmenting %d%% complete", i / futures.size());
                }

                this.taskRepository.update(this.task.getId(), Status.SUCCESS, "Augmenting 100% complete");
                this.callback.finished(points);
            } catch (InterruptedException | ExecutionException e) {
                this.logger.error(e.getMessage(), e);
                this.taskRepository.update(this.task.getId(), Status.FAILURE, e.getMessage());
            }
        }

        private List<Future<List<Point>>> startAugments() {
            List<Future<List<Point>>> futures = new ArrayList<>(this.encodedPolylines.size());

            for (int i = 0; i < this.encodedPolylines.size(); i++) {
                PointAugmentingCallable callable = new PointAugmentingCallable(this.restTemplate, ELEVATION_URL + this.encodedPolylines.get(i));
                long delay = i * DELAY_INCREMENT;

                ScheduledFuture<List<Point>> future = this.scheduledExecutorService.schedule(callable, delay, TimeUnit.MILLISECONDS);

                futures.add(future);
            }

            return futures;
        }

    }

    private static final class PointAugmentingCallable implements Callable<List<Point>> {

        private final RestTemplate restTemplate;

        private final String uri;

        private PointAugmentingCallable(RestTemplate restTemplate, String uri) {
            this.restTemplate = restTemplate;
            this.uri = uri;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public List<Point> call() throws Exception {
            ResponseEntity<Map> response = this.restTemplate.getForEntity(this.uri, Map.class);

            String status = (String) response.getBody().get("status");
            if (!"OK".equals(status)) {
                throw new IllegalStateException(String.format("Point augmentation failed with a status of '%s'", status));
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            List<Point> points = new ArrayList(results.size());
            for (Map<String, Object> result : results) {
                Map<String, Double> location = (Map<String, Double>) result.get("location");
                Double latitude = location.get("lat");
                Double longitude = location.get("lng");
                Double elevation = (Double) result.get("elevation");

                points.add(new Point(latitude, longitude, elevation));
            }

            return points;
        }
    }

}
