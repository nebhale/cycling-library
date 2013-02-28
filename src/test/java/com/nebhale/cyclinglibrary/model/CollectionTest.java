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

public class CollectionTest {

    @Test
    public void test() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2), Long.valueOf(3));

        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertEquals("test-short-name", collection.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
        assertEquals("Collection [typeId=0, name=test-name, shortName=test-short-name, itemIds=[2, 3], getId()=1]", collection.toString());
    }

}
