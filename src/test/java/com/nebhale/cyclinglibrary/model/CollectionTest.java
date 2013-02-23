
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionTest {

    @Test
    public void test() {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name");

        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertEquals("Collection [typeId=0, name=test-name, getId()=1]", collection.toString());
    }

}
