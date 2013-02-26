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

import com.nebhale.cyclinglibrary.util.Sets;

public class TypeTest {

    @Test
    public void test() {
        Type type = new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2));

        assertEquals("test-name", type.getName());
        assertEquals(Sets.asSet(Long.valueOf(1), Long.valueOf(2)), type.getCollectionIds());
        assertEquals("Type [name=test-name, collectionIds=[1, 2], getId()=0]", type.toString());
    }

}
