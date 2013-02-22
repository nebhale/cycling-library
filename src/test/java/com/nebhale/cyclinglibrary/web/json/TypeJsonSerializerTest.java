
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
        return new Type(0, "test-name");
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.name", "test-name");
        assertValue(result, "$.links[?(@.rel== 'self')].href", "http://localhost/types/0");
    }

}
