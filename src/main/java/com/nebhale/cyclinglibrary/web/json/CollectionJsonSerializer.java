
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nebhale.cyclinglibrary.model.Collection;

@Component
final class CollectionJsonSerializer extends StdSerializer<Collection> {

    CollectionJsonSerializer() {
        super(Collection.class);
    }

    @Override
    public void serialize(Collection value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getName());

        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "types", value.getTypeId(), "collections", value));
        jgen.writeEndArray();

        jgen.writeEndObject();
    }

}
