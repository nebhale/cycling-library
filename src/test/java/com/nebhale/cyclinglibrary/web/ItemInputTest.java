
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ItemInputTest {

    @Test
    public void test() {
        ItemInput itemInput = new ItemInput("test-name");

        assertEquals("test-name", itemInput.getName());
        assertEquals("ItemInput [name=test-name]", itemInput.toString());
    }

}
