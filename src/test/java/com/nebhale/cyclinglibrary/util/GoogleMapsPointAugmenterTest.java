
package com.nebhale.cyclinglibrary.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

public class GoogleMapsPointAugmenterTest {

    private final PolylineEncoder polylineEncoder = mock(PolylineEncoder.class);

    private final RestTemplate restTemplate = mock(RestTemplate.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    private final TaskRepository taskRepository = mock(TaskRepository.class);

    private final GoogleMapsPointAugmenter pointAugmenter = new GoogleMapsPointAugmenter(this.polylineEncoder, this.restTemplate,
        this.scheduledExecutorService, this.taskRepository);

    @Test
    @SuppressWarnings("rawtypes")
    public void augmentPoints() throws InterruptedException {
        Double[][] points = new Double[0][0];
        List<String> encodedPolylines = Arrays.asList("encoded-polyline-0");
        when(this.polylineEncoder.encode(1973, points)).thenReturn(encodedPolylines);

        Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");
        when(this.taskRepository.create("Augmenting %d segments", 1)).thenReturn(task);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> location = new HashMap<>();
        location.put("lat", 1.0);
        location.put("lng", 2.0);
        result.put("location", location);
        result.put("elevation", 3.0);
        results.add(result);
        response.put("results", results);

        when(
            this.restTemplate.getForEntity("https://maps.googleapis.com/maps/api/elevation/json?sensor=false&locations=encoded-polyline-0", Map.class)).thenReturn(
            new ResponseEntity<Map>(response, HttpStatus.OK));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        StubPointAugmenterCallback callback = new StubPointAugmenterCallback(countDownLatch);
        this.pointAugmenter.augmentPoints(points, callback);

        countDownLatch.await();

        assertEquals(1, callback.points.size());
        Point point = callback.points.get(0);
        assertEquals(Double.valueOf(1.0), point.getLatitude());
        assertEquals(Double.valueOf(2.0), point.getLongitude());
        assertEquals(Double.valueOf(3.0), point.getElevation());

        verify(this.taskRepository).update(Long.valueOf(0), Status.SUCCESS, "Augmenting 100% complete");
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void augmentPointsFailure() throws InterruptedException {
        Double[][] points = new Double[0][0];
        List<String> encodedPolylines = Arrays.asList("encoded-polyline-0");
        when(this.polylineEncoder.encode(1973, points)).thenReturn(encodedPolylines);

        Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");
        when(this.taskRepository.create("Augmenting %d segments", 1)).thenReturn(task);

        Map<String, String> response = new HashMap<>();
        response.put("status", "FAIL");
        when(
            this.restTemplate.getForEntity("https://maps.googleapis.com/maps/api/elevation/json?sensor=false&locations=encoded-polyline-0", Map.class)).thenReturn(
            new ResponseEntity<Map>(response, HttpStatus.OK));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.pointAugmenter.augmentPoints(points, new StubPointAugmenterCallback(countDownLatch));

        boolean result = countDownLatch.await(250, TimeUnit.MILLISECONDS);
        assertFalse(result);
        verify(this.taskRepository).update(Long.valueOf(0), Status.FAILURE,
            "java.lang.IllegalStateException: Point augmentation failed with a status of 'FAIL'");
    }

    private static final class StubPointAugmenterCallback implements PointAugmenterCallback {

        private final CountDownLatch countDownLatch;

        private volatile List<Point> points;

        private StubPointAugmenterCallback(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void finished(List<Point> points) {
            this.points = points;
            this.countDownLatch.countDown();
        }

    }

}
