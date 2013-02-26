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

package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TaskTest {

    @Test
    public void test() {
        Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");

        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals("test-message", task.getMessage());
        assertEquals("Task [status=IN_PROGRESS, message=test-message, getId()=0]", task.toString());
    }

}
