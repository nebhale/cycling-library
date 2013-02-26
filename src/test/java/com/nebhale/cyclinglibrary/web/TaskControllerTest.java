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

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

public final class TaskControllerTest {

    private final TaskRepository taskRepository = mock(TaskRepository.class);

    private final TaskController controller = new TaskController(this.taskRepository);

    @Test
    public void find() {
        Set<Task> tasks = Collections.emptySet();
        when(this.taskRepository.find()).thenReturn(tasks);

        assertSame(tasks, this.controller.find());
    }

    @Test
    public void read() {
        Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");
        when(this.taskRepository.read(Long.valueOf(0))).thenReturn(task);

        Task result = this.controller.read(Long.valueOf(0));

        assertSame(task, result);
    }

    @Test
    public void delete() {
        this.controller.delete(Long.valueOf(0));

        verify(this.taskRepository).delete(Long.valueOf(0));
    }

}
