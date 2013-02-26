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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nebhale.cyclinglibrary.model.StubIdentifiable;

public class LinkTest {

    @Before
    public void setRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @After
    public void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void test() {
        Link link = new Link("test-rel", new StubIdentifiable(Long.valueOf(0)), "path-segment", new Stub());

        assertEquals("test-rel", link.getRel());
        assertEquals("http://localhost/0/path-segment/non-identifiable", link.getHref());
        assertEquals("Link [rel=test-rel, href=http://localhost/0/path-segment/non-identifiable]", link.toString());

    }

    private static final class Stub {

        @Override
        public String toString() {
            return "non-identifiable";
        }
    }

}
