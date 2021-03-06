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
        jgen.writeStringField("shortName", value.getShortName());
        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "types", value.getTypeId(), "collections", value.getCollectionId(), "items", value));
        jgen.writeObject(new Link("points-raw", "types", value.getTypeId(), "collections", value.getCollectionId(), "items", value, "points"));
        jgen.writeObject(new Link("points-image", "types", value.getTypeId(), "collections", value.getCollectionId(), "items", value, "points"));
        jgen.writeObject(new Link("points-gpx", "types", value.getTypeId(), "collections", value.getCollectionId(), "items", value, "points"));
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
