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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

final class GzipFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
        IOException {

        HttpServletRequest newRequest;
        if (sendsGzipEncoding(request)) {
            newRequest = new DelegatingHttpServletRequest(request, new GZIPInputStream(request.getInputStream()));
        } else {
            newRequest = request;
        }

        HttpServletResponse newResponse;
        if (acceptsGzipEncoding(request)) {
            newResponse = new DelegatingHttpServletResponse(response, new GZIPOutputStream(response.getOutputStream()));
        } else {
            newResponse = response;
        }

        // TODO Auto-generated method stub

    }

    private boolean sendsGzipEncoding(HttpServletRequest request) {
        return isGzipEncoding(request.getHeader("Content-Encoding"));
    }

    private boolean acceptsGzipEncoding(HttpServletRequest request) {
        return isGzipEncoding(request.getHeader("Accept-Encoding"));

    }

    private boolean isGzipEncoding(String encoding) {
        return (encoding != null) && "gzip".equals(encoding);
    }

}
