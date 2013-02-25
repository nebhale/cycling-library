
package com.nebhale.cyclinglibrary.repository;

import java.util.Set;

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

/**
 * A repository for accessing tasks in the library
 */
public interface TaskRepository {

    /**
     * Find all tasks
     * 
     * @return All tasks
     */
    Set<Task> find();

    /**
     * Create a new task
     * 
     * @param format The message format string
     * @param arguments The arguments for the message format
     * @return The created task
     */
    Task create(String format, Object... arguments);

    /**
     * Read a task identified by its id
     * 
     * @param taskId The id of the task to read
     * @return The task
     */
    Task read(Long taskId);

    /**
     * Update a task identified by its id
     * 
     * @param taskId The id of the task to update
     * @param status The status of the task
     * @param format The message format string
     * @param arguments The arguments for the message format
     * @return The updated task
     */
    Task update(Long taskId, Status status, String format, Object... arguments);

    /**
     * Delete a task identified by its id
     * 
     * @param taskId The id of the task to delete
     */
    void delete(Long taskId);
}
