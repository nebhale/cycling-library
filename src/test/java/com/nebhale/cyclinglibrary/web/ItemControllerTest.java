
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.repository.ItemRepository;

public final class ItemControllerTest {

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final ItemController controller = new ItemController(this.itemRepository);

    @Test
    public void create() {
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name");
        when(this.itemRepository.create(Long.valueOf(1), "test-name")).thenReturn(item);

        Item result = this.controller.create(Long.valueOf(1), new ItemInput("test-name"));

        assertSame(item, result);
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
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "new-test-name");
        when(this.itemRepository.update(Long.valueOf(2), "new-test-name")).thenReturn(item);

        Item result = this.controller.update(Long.valueOf(2), new ItemInput("new-test-name"));

        assertSame(item, result);
    }

    @Test
    public void delete() {
        this.controller.delete(Long.valueOf(2));

        verify(this.itemRepository).delete(Long.valueOf(2));
    }

}
