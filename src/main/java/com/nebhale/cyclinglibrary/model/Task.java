
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
        return status;
    }

    /**
     * Returns a message about the task
     * 
     * @return a message about the task
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Task [status=" + status + ", message=" + message + ", getId()=" + getId() + "]";
    }

}
