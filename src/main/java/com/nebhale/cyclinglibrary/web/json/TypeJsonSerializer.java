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
import com.nebhale.cyclinglibrary.model.Type;

@Component
final class TypeJsonSerializer extends StdSerializer<Type> {

    TypeJsonSerializer() {
        super(Type.class);
    }

    @Override
    public void serialize(Type value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.getName());
        jgen.writeArrayFieldStart("links");
        jgen.writeObject(new Link("self", "types", value));
        for (Long collectiondId : value.getCollectionIds()) {
            jgen.writeObject(new Link("collection", "types", value, "collections", collectiondId));
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
