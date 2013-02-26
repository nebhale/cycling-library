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

public class LinkJsonSerializerTest extends AbstractJsonSerializerTest<Link> {

    @Override
    protected JsonSerializer<Link> getJsonSerializer() {
        return new LinkJsonSerializer();
    }

    @Override
    protected Link getValue() {
        return new Link("test-rel", "path", "segments");
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.rel", "test-rel");
        assertValue(result, "$.href", "http://localhost/path/segments");
    }

}
