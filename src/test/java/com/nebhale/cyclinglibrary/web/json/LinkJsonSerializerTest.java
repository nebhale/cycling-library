
package com.nebhale.cyclinglibrary.web.json;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonSerializer;

public class LinkJsonSerializerTest extends AbstractJsonSerializerTest<Link> {

    @Override
    protected JsonSerializer<Link> getJsonSerializer() {
        return new LinkJsonSerializer();
    }

    @Override
    protected Link getValue() {
        return new Link("test-rel", "path", "segments");
    }

    @Override
    protected void assertResult(String result) throws ParseException {
        assertValue(result, "$.rel", "test-rel");
        assertValue(result, "$.href", "http://localhost/path/segments");
    }

}
