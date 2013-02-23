
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionInputTest {

    @Test
    public void test() {
        CollectionInput collectionInput = new CollectionInput("test-name");

        assertEquals("test-name", collectionInput.getName());
        assertEquals("CollectionInput [name=test-name]", collectionInput.toString());
    }

}
