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

    private final CollectionController controller = new CollectionController(this.collectionRepository);

    @Test
    public void create() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name");
        when(this.collectionRepository.create(Long.valueOf(0), "test-name", "test-short-name")).thenReturn(collection);

        Collection result = this.controller.create(Long.valueOf(0), new CollectionInput("test-name", "test-short-name"));

        assertSame(collection, result);
    }

    @Test
    public void read() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name");
        when(this.collectionRepository.read(Long.valueOf(1))).thenReturn(collection);

        Collection result = this.controller.read(Long.valueOf(1));

        assertSame(collection, result);
    }

    @Test
    public void update() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "new-test-name", "new-test-short-name");
        when(this.collectionRepository.update(Long.valueOf(1), "new-test-name", "new-test-short-name")).thenReturn(collection);

        Collection result = this.controller.update(Long.valueOf(1), new CollectionInput("new-test-name", "new-test-short-name"));

        assertSame(collection, result);
    }

    @Test
    public void delete() {
        this.controller.delete(Long.valueOf(1));

        verify(this.collectionRepository).delete(Long.valueOf(1));
    }

}
