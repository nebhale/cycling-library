
package com.nebhale.cyclinglibrary.web.json;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nebhale.cyclinglibrary.model.Type;

public class TypeJsonSerializerTest extends AbstractJsonSerializerTest<Type> {

    @Override
    protected JsonSerializer<Type> getJsonSerializer() {
        return new TypeJsonSerializer();
    }

    @Override
    protected Type getValue() {
        return new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2));
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.name", "test-name");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/types/0");
        assertValue(result, "$.links[?(@.rel== 'collection')].href", "http://localhost/types/0/collections/1",
            "http://localhost/types/0/collections/2");
    }

}
