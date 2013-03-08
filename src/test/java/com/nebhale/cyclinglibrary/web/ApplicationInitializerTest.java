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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.DispatcherServlet;

public final class ApplicationInitializerTest {

    private final ServletContext servletContext = mock(ServletContext.class);

    private final ServletRegistration.Dynamic dispatcherServlet = mock(ServletRegistration.Dynamic.class);

    private final FilterRegistration.Dynamic gzipFilter = mock(FilterRegistration.Dynamic.class);

    private final FilterRegistration.Dynamic eTagFilter = mock(FilterRegistration.Dynamic.class);

    private final ApplicationInitializer initializer = new ApplicationInitializer();

    @Test
    public void onStartup() throws ServletException {
        when(this.servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class))).thenReturn(this.dispatcherServlet);
        when(this.servletContext.addFilter(eq("gzip"), any(GzipFilter.class))).thenReturn(this.gzipFilter);
        when(this.servletContext.addFilter(eq("etag"), any(ShallowEtagHeaderFilter.class))).thenReturn(this.eTagFilter);

        this.initializer.onStartup(this.servletContext);

        verify(this.servletContext).addListener(any(ContextLoaderListener.class));
        verify(this.dispatcherServlet).setLoadOnStartup(1);
        verify(this.dispatcherServlet).addMapping("/");

        verify(this.gzipFilter).addMappingForServletNames(null, false, "dispatcher");
        verify(this.eTagFilter).addMappingForServletNames(null, false, "dispatcher");
    }

}
