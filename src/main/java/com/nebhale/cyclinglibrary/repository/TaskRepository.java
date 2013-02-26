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
