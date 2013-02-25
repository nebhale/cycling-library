
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ItemInputTest {

    @Test
    public void test() {
        ItemInput itemInput = new ItemInput("test-name", "test-points");

        assertEquals("test-name", itemInput.getName());
        assertEquals("test-points", itemInput.getPoints());
        assertEquals("ItemInput [name=test-name, points=test-points]", itemInput.toString());
    }

}
