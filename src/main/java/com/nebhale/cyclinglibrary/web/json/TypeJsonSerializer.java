
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nebhale.cyclinglibrary.model.Type;

@Component
final class TypeJsonSerializer extends StdSerializer<Type> {

    TypeJsonSerializer() {
        super(Type.class);
    }

    @Override
    public void serialize(Type value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getName());

        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "types", value));
        jgen.writeEndArray();

        jgen.writeEndObject();
    }

}
