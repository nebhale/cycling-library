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

package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class GzipFilterTest {

    private final GzipFilter filter = new GzipFilter();

    @Test
    public void gzipRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Content-Encoding", "gzip");
        request.setContent(gzipContent("test-request-content"));

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilterInternal(request, response, filterChain);
        writeContent("test-response-content", filterChain.getResponse().getOutputStream());

        assertEquals("test-request-content", readContent(filterChain.getRequest().getInputStream()));
        assertEquals("test-response-content", response.getContentAsString());
    }

    @Test
    public void gzipResponse() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Encoding", "gzip");
        request.setContent("test-request-content".getBytes("UTF-8"));

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilterInternal(request, response, filterChain);
        writeContent("test-response-content", filterChain.getResponse().getOutputStream());

        assertEquals("test-request-content", readContent(filterChain.getRequest().getInputStream()));
        assertEquals("test-response-content", gunzipContent(response.getContentAsByteArray()));
    }

    private byte[] gzipContent(String content) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new ByteArrayInputStream(content.getBytes("UTF-8"));

            out = new GZIPOutputStream(bytes, true);

            copy(in, out);
        } finally {
            closeQuietly(in, out);
        }

        return bytes.toByteArray();
    }

    private String gunzipContent(byte[] content) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            in = new GZIPInputStream(new ByteArrayInputStream(content));
            out = new ByteArrayOutputStream();

            copy(in, out);
            return out.toString("UTF-8");
        } finally {
            closeQuietly(in, out);
        }
    }

    private String readContent(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();

            copy(in, out);
            return out.toString("UTF-8");
        } finally {
            closeQuietly(in, out);
        }
    }

    private void writeContent(String content, OutputStream out) throws IOException {
        ByteArrayInputStream in = null;

        try {
            in = new ByteArrayInputStream(content.getBytes("UTF-8"));

            copy(in, out);
        } finally {
            closeQuietly(in, out);
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
    }

    private void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
