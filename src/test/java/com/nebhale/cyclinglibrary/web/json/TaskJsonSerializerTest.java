
package com.nebhale.cyclinglibrary.web.json;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

public class TaskJsonSerializerTest extends AbstractJsonSerializerTest<Task> {

    @Override
    protected JsonSerializer<Task> getJsonSerializer() {
        return new TaskJsonSerializer();
    }

    @Override
    protected Task getValue() {
        return new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.status", "IN_PROGRESS");
        assertValue(result, "$.message", "test-message");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/tasks/0");
    }

}
