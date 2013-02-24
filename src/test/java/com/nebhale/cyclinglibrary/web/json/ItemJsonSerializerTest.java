
package com.nebhale.cyclinglibrary.web.json;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nebhale.cyclinglibrary.model.Item;

public class ItemJsonSerializerTest extends AbstractJsonSerializerTest<Item> {

    @Override
    protected JsonSerializer<Item> getJsonSerializer() {
        return new ItemJsonSerializer();
    }

    @Override
    protected Item getValue() {
        return new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name");
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.name", "test-name");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/types/0/collections/1/items/2");
    }

}
