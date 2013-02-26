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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointTest {

    @Test
    public void test() {
        Point point = new Point(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Double.valueOf(4), Double.valueOf(5),
            Double.valueOf(6));

        assertEquals(Long.valueOf(0), point.getTypeId());
        assertEquals(Long.valueOf(1), point.getCollectionId());
        assertEquals(Long.valueOf(2), point.getItemId());
        assertEquals(Double.valueOf(4), point.getLatitude());
        assertEquals(Double.valueOf(5), point.getLongitude());
        assertEquals(Double.valueOf(6), point.getElevation());
        assertEquals("Point [typeId=0, collectionId=1, itemId=2, latitude=4.0, longitude=5.0, elevation=6.0, getId()=3]", point.toString());

        Point greaterThan = new Point(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), Long.valueOf(7), Double.valueOf(4), Double.valueOf(5),
            Double.valueOf(6));
        Point equal = new Point(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Double.valueOf(4), Double.valueOf(5),
            Double.valueOf(6));
        assertTrue(point.compareTo(greaterThan) < 0);
        assertTrue(greaterThan.compareTo(point) > 0);
        assertTrue(point.compareTo(equal) == 0);
    }
}
