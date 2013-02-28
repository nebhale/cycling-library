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

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nebhale.cyclinglibrary.model.Collection;

public class CollectionJsonSerializerTest extends AbstractJsonSerializerTest<Collection> {

    @Override
    protected JsonSerializer<Collection> getJsonSerializer() {
        return new CollectionJsonSerializer();
    }

    @Override
    protected Collection getValue() {
        return new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2), Long.valueOf(3));
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.name", "test-name");
        assertValue(result, "$.shortName", "test-short-name");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/types/0/collections/1");
        assertValue(result, "$.links[?(@.rel== 'item')].href", "http://localhost/types/0/collections/1/items/2",
            "http://localhost/types/0/collections/1/items/3");
    }

}
