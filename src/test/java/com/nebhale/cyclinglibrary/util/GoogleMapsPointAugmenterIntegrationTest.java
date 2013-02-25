
package com.nebhale.cyclinglibrary.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { UtilConfiguration.class, IntegrationTestUtilConfiguration.class })
public class GoogleMapsPointAugmenterIntegrationTest {

    @Autowired
    private volatile GoogleMapsPointAugmenter pointAugmenter;

    @Autowired
    private volatile TaskRepository taskRepository;

    @Test
    public void augmentPoints() throws InterruptedException {
        when(this.taskRepository.create("Augmenting %d segments", 1)).thenReturn(new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message"));

        Double[][] points = new Double[][] { //
        new Double[] { 38.5, -120.2 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 43.252, -126.453 } };

        CountDownLatch countDownLatch = new CountDownLatch(1);
        StubPointAugmenterCallback callback = new StubPointAugmenterCallback(countDownLatch);

        this.pointAugmenter.augmentPoints(points, callback);
        countDownLatch.await();

        assertEquals(3, callback.points.size());

        Point point0 = callback.points.get(0);
        assertEquals(Double.valueOf(38.5), point0.getLatitude());
        assertEquals(Double.valueOf(-120.2), point0.getLongitude());
        assertEquals(Double.valueOf(1204.842163085938), point0.getElevation());

        Point point1 = callback.points.get(1);
        assertEquals(Double.valueOf(40.7), point1.getLatitude());
        assertEquals(Double.valueOf(-120.95), point1.getLongitude());
        assertEquals(Double.valueOf(1694.130737304688), point1.getElevation());

        Point point2 = callback.points.get(2);
        assertEquals(Double.valueOf(43.252), point2.getLatitude());
        assertEquals(Double.valueOf(-126.453), point2.getLongitude());
        assertEquals(Double.valueOf(-2984.89794921875), point2.getElevation());
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
