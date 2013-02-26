/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.cyclinglibrary.web.json;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Arrays;

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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addSerializer(new LinkJsonSerializer()));

        T value = getValue();
        StringWriter out = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createJsonGenerator(out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        try {
            getJsonSerializer().serialize(value, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
            assertResult(out.toString());
        } finally {
            out.close();
            jsonGenerator.close();
        }
    }

    protected final void assertValue(String result, String expression, Object... value) throws ParseException {
        Object normalizedValue = value.length == 1 ? value[0] : Arrays.asList(value);
        new JsonPathExpectationsHelper(expression).assertValue(result, normalizedValue);
    }

    protected abstract JsonSerializer<T> getJsonSerializer();

    protected abstract T getValue();

    protected abstract void assertResult(String result) throws ParseException;
}
