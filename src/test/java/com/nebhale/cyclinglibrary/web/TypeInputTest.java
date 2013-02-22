
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeInputTest {

    @Test
    public void test() {
        TypeInput typeInput = new TypeInput("test-name");
        
        assertEquals("test-name", typeInput.getName());
        assertEquals("TypeInput [name=test-name]", typeInput.toString());
    }

}
