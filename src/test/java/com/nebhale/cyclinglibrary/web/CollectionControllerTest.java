
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;

public final class CollectionControllerTest {

    private final CollectionRepository collectionRepository = mock(CollectionRepository.class);

    private final CollectionController controller = new CollectionController(collectionRepository);

    @Test
    public void create() {
        Collection collection = new Collection(0, 1, "test-name");
        when(this.collectionRepository.create(0, "test-name")).thenReturn(collection);

        Collection result = this.controller.create(0, new CollectionInput("test-name"));

        assertSame(collection, result);
    }

    @Test
    public void read() {
        Collection collection = new Collection(0, 1, "test-name");
        when(this.collectionRepository.read(1)).thenReturn(collection);

        Collection result = this.controller.read(1);

        assertSame(collection, result);
    }

    @Test
    public void update() {
        Collection collection = new Collection(0, 1, "new-test-name");
        when(this.collectionRepository.update(1, "new-test-name")).thenReturn(collection);

        Collection result = this.controller.update(1, new CollectionInput("new-test-name"));

        assertSame(collection, result);
    }

    @Test
    public void delete() {
        this.controller.delete(1);

        verify(this.collectionRepository).delete(1);
    }

}
