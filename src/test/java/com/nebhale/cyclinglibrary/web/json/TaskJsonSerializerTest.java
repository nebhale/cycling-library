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
