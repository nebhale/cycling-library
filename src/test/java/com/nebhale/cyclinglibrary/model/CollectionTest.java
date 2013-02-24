
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTest {

    @Test
    public void test() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", Long.valueOf(2), Long.valueOf(3));

        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertArrayEquals(new Long[] { Long.valueOf(2), Long.valueOf(3) }, collection.getItemIds());
        assertEquals("Collection [typeId=0, name=test-name, itemIds=[2, 3], getId()=1]", collection.toString());
    }

}
