
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
