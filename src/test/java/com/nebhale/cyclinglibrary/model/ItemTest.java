
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ItemTest {

    @Test
    public void test() {
        Point point0 = new Point(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Double.valueOf(4), Double.valueOf(5),
            Double.valueOf(6));
        Point point1 = new Point(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), Long.valueOf(7), Double.valueOf(8), Double.valueOf(9),
            Double.valueOf(10));

        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", point0, point1);

        assertEquals(Long.valueOf(0), item.getTypeId());
        assertEquals(Long.valueOf(1), item.getCollectionId());
        assertEquals("test-name", item.getName());
        assertEquals(Arrays.asList(point0, point1), item.getPoints());
        assertEquals(
            "Item [typeId=0, collectionId=1, name=test-name, points=[Point [typeId=0, collectionId=1, itemId=2, latitude=4.0, longitude=5.0, elevation=6.0, getId()=3], Point [typeId=0, collectionId=1, itemId=2, latitude=8.0, longitude=9.0, elevation=10.0, getId()=7]], getId()=2]",
            item.toString());
    }
}
