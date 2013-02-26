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

package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ItemInputTest {

    @Test
    public void test() {
        ItemInput itemInput = new ItemInput("test-name", "test-points");

        assertEquals("test-name", itemInput.getName());
        assertEquals("test-points", itemInput.getPoints());
        assertEquals("ItemInput [name=test-name, points=test-points]", itemInput.toString());
    }

}
