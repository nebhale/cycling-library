
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nebhale.cyclinglibrary.model.Task;

@Component
final class TaskJsonSerializer extends StdSerializer<Task> {

    TaskJsonSerializer() {
        super(Task.class);
    }

    @Override
    public void serialize(Task value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("status", value.getStatus().toString());
        jgen.writeStringField("message", value.getMessage());
        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "tasks", value));
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
