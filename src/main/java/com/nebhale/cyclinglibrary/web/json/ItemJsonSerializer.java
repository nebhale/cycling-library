
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nebhale.cyclinglibrary.model.Item;

@Component
final class ItemJsonSerializer extends StdSerializer<Item> {

    ItemJsonSerializer() {
        super(Item.class);
    }

    @Override
    public void serialize(Item value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getName());
        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "types", value.getTypeId(), "collections", value.getCollectionId(), "items", value));
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
