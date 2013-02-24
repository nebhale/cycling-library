
package com.nebhale.cyclinglibrary.web.json;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nebhale.cyclinglibrary.model.Collection;

public class CollectionJsonSerializerTest extends AbstractJsonSerializerTest<Collection> {

    @Override
    protected JsonSerializer<Collection> getJsonSerializer() {
        return new CollectionJsonSerializer();
    }

    @Override
    protected Collection getValue() {
        return new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", Long.valueOf(2), Long.valueOf(3));
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.name", "test-name");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/types/0/collections/1");
        assertValue(result, "$.links[?(@.rel== 'item')].href", "http://localhost/types/0/collections/1/items/2",
            "http://localhost/types/0/collections/1/items/3");
    }

}
