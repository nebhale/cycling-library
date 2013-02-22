
package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class AbstractJsonSerializerTest<T> {

    @Before
    public final void setRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @After
    public final void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public final void test() throws IOException, ParseException {
        StringWriter out = new StringWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addSerializer(new LinkJsonSerializer()));

        T value = getValue();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createJsonGenerator(out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        getJsonSerializer().serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertResult(out.toString());
    }

    protected final void assertValue(String result, String expression, String value) throws ParseException {
        new JsonPathExpectationsHelper(expression).assertValue(result, value);
    }

    protected abstract JsonSerializer<T> getJsonSerializer();

    protected abstract T getValue();

    protected abstract void assertResult(String result) throws ParseException;
}
