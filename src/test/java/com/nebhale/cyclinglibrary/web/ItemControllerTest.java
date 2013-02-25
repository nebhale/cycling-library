
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PointParser;
import com.nebhale.cyclinglibrary.util.PointParserCallback;

public final class ItemControllerTest {

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final PointParser pointParser = mock(PointParser.class);

    private final ItemController controller = new ItemController(this.itemRepository, this.pointParser);

    @Test
    public void create() {
        Task task = new Task(Long.valueOf(4), Status.IN_PROGRESS, "test-message");
        ArgumentCaptor<PointParserCallback> callback = ArgumentCaptor.forClass(PointParserCallback.class);
        when(this.pointParser.parse(eq("test-points"), callback.capture())).thenReturn(task);
        List<Point> points = Arrays.asList();

        Task result = this.controller.create(Long.valueOf(1), new ItemInput("test-name", "test-points"));
        callback.getValue().finished(points);

        assertSame(task, result);
        verify(this.itemRepository).create(Long.valueOf(1), "test-name", points);
    }

    @Test
    public void read() {
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name");
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(item);

        Item result = this.controller.read(Long.valueOf(2));

        assertSame(item, result);
    }

    @Test
    public void update() {
        Task task = new Task(Long.valueOf(4), Status.IN_PROGRESS, "test-message");
        ArgumentCaptor<PointParserCallback> callback = ArgumentCaptor.forClass(PointParserCallback.class);
        when(this.pointParser.parse(eq("new-test-points"), callback.capture())).thenReturn(task);
        List<Point> points = Arrays.asList();

        Task result = this.controller.update(Long.valueOf(2), new ItemInput("new-test-name", "new-test-points"));
        callback.getValue().finished(points);

        assertSame(task, result);
        verify(this.itemRepository).update(Long.valueOf(2), "new-test-name", points);
    }

    @Test
    public void delete() {
        this.controller.delete(Long.valueOf(2));

        verify(this.itemRepository).delete(Long.valueOf(2));
    }

}
