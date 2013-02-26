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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractIdentifableSupportTest {

    @Test
    public void test() {
        StubIdentifiable stub = new StubIdentifiable(Long.valueOf(0));
        StubIdentifiable equal = new StubIdentifiable(Long.valueOf(0));
        StubIdentifiable notEqual = new StubIdentifiable(Long.valueOf(1));
        StubIdentifiable nullId = new StubIdentifiable(null);
        StubIdentifiable otherNullId = new StubIdentifiable(null);

        assertEquals(Long.valueOf(0), stub.getId());

        assertEquals(stub.hashCode(), equal.hashCode());
        assertNotEquals(stub.hashCode(), notEqual.hashCode());
        assertEquals(nullId.hashCode(), otherNullId.hashCode());

        assertTrue(stub.equals(stub));
        assertFalse(stub.equals(null));
        assertFalse(stub.equals(new Object()));
        assertFalse(nullId.equals(stub));
        assertTrue(nullId.equals(otherNullId));
        assertFalse(stub.equals(notEqual));
        assertTrue(stub.equals(equal));
    }
}
