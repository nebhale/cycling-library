
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ItemTest {

    @Test
    public void test() {
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name");

        assertEquals(Long.valueOf(0), item.getTypeId());
        assertEquals(Long.valueOf(1), item.getCollectionId());
        assertEquals("test-name", item.getName());
        assertEquals("Item [typeId=0, collectionId=1, name=test-name, getId()=2]", item.toString());
    }

}
