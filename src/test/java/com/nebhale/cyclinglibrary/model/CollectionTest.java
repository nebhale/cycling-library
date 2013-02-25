
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.nebhale.cyclinglibrary.util.Sets;

public class CollectionTest {

    @Test
    public void test() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", Long.valueOf(2), Long.valueOf(3));

        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
        assertEquals("Collection [typeId=0, name=test-name, itemIds=[2, 3], getId()=1]", collection.toString());
    }

}
