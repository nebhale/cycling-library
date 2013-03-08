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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

final class GzipFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
        IOException {

        HttpServletRequest newRequest;
        if (sendsGzipEncoding(request)) {
            newRequest = new DelegatingHttpServletRequest(request, new GZIPInputStream(request.getInputStream()));
        } else {
            String method = request.getMethod();
            if ("POST".equals(method) || "PUT".equals(method)) {
                this.logger.warn("Uncompressed input received for '{} {}'", method, request.getRequestURI());
            }

            newRequest = request;
        }

        HttpServletResponse newResponse;
        OutputStream outputStream;
        if (acceptsGzipEncoding(request)) {
            response.setHeader("Content-Encoding", "gzip");
            outputStream = new GZIPOutputStream(response.getOutputStream());
            newResponse = new DelegatingHttpServletResponse(response, outputStream);
        } else {
            this.logger.warn("Uncompressed output requested for '{} {}'", request.getMethod(), request.getRequestURI());
            outputStream = response.getOutputStream();
            newResponse = response;
        }

        filterChain.doFilter(newRequest, newResponse);

        newResponse.flushBuffer();
        outputStream.close();
    }

    private boolean sendsGzipEncoding(HttpServletRequest request) {
        return isGzipEncoding(request.getHeader("Content-Encoding"));
    }

    private boolean acceptsGzipEncoding(HttpServletRequest request) {
        return isGzipEncoding(request.getHeader("Accept-Encoding"));
    }

    private boolean isGzipEncoding(String encoding) {
        return (encoding != null) && encoding.contains("gzip");
    }

    private static final class DelegatingHttpServletRequest extends HttpServletRequestWrapper {

        private final ServletInputStream inputStream;

        private DelegatingHttpServletRequest(HttpServletRequest delegate, InputStream inputStream) {
            super(delegate);
            this.inputStream = new DelegatingServletInputStream(inputStream);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return this.inputStream;
        }

    }

    private static final class DelegatingServletInputStream extends ServletInputStream {

        private final InputStream sourceStream;

        private DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        @Override
        public int read() throws IOException {
            return this.sourceStream.read();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.sourceStream.close();
        }

    }

    private static final class DelegatingHttpServletResponse extends HttpServletResponseWrapper {

        private final ServletOutputStream outputStream;

        private DelegatingHttpServletResponse(HttpServletResponse response, OutputStream outputStream) {
            super(response);
            this.outputStream = new DelegatingServletOutputStream(outputStream);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return this.outputStream;
        }
    }

    private static final class DelegatingServletOutputStream extends ServletOutputStream {

        private final OutputStream targetStream;

        private DelegatingServletOutputStream(OutputStream targetStream) {
            this.targetStream = targetStream;
        }

        @Override
        public void write(int b) throws IOException {
            this.targetStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

    }

}
