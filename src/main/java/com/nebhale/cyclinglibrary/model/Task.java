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

package com.nebhale.cyclinglibrary.model;

/**
 * Represents a top-level type in the library
 */
public final class Task extends AbstractIdentifableSupport {

    private final Status status;

    private final String message;

    /**
     * Creates a new instance specifying the id, status, and message
     * 
     * @param id The id of the task
     * @param status The status of the task
     * @param message A message about the tasks
     */
    public Task(Long id, Status status, String message) {
        super(id);
        this.status = status;
        this.message = message;
    }

    /**
     * Returns the status of the task
     * 
     * @return the status of the task
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Returns a message about the task
     * 
     * @return a message about the task
     */
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "Task [status=" + this.status + ", message=" + this.message + ", getId()=" + getId() + "]";
    }

}
