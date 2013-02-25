
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TaskTest {

    @Test
    public void test() {
        Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");

        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals("test-message", task.getMessage());
        assertEquals("Task [status=IN_PROGRESS, message=test-message, getId()=0]", task.toString());
    }

}
