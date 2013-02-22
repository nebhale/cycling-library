
package com.nebhale.cyclinglibrary.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeTest {

    @Test
    public void test() {
        Type type = new Type(0, "test-name");

        assertEquals("test-name", type.getName());
        assertEquals("Type [name=test-name, getId()=0]", type.toString());
    }

}
