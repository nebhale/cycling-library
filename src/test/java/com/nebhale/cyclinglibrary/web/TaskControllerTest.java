
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
