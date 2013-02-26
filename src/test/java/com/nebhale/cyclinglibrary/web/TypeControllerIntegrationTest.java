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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nebhale.cyclinglibrary.model.Type;
import com.nebhale.cyclinglibrary.repository.TypeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class TypeControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile TypeRepository typeRepository;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void find() throws Exception {
        Set<Type> types = new HashSet<>();
        types.add(new Type(Long.valueOf(0), "test-name-1", Long.valueOf(1), Long.valueOf(2)));
        when(this.typeRepository.find()).thenReturn(types);

        this.mockMvc.perform(get("/types").accept(ApplicationMediaType.TYPE)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.TYPE)) //
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create() throws Exception {
        when(this.typeRepository.create("test-name")).thenReturn(new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2)));

        this.mockMvc.perform(
            post("/types").contentType(ApplicationMediaType.TYPE).content("{\"name\":\"test-name\"}").accept(ApplicationMediaType.TYPE)) //
        .andExpect(status().isCreated()) //
        .andExpect(content().contentType(ApplicationMediaType.TYPE)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'collection')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1", "http://localhost/types/0/collections/2")));
    }

    @Test
    public void read() throws Exception {
        when(this.typeRepository.read(Long.valueOf(0))).thenReturn(new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2)));

        this.mockMvc.perform(get("/types/{typeId}", 0).accept(ApplicationMediaType.TYPE)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.TYPE)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'collection')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1", "http://localhost/types/0/collections/2")));
    }

    @Test
    public void update() throws Exception {
        when(this.typeRepository.update(Long.valueOf(0), "new-test-name")).thenReturn(
            new Type(Long.valueOf(0), "new-test-name", Long.valueOf(1), Long.valueOf(2)));

        this.mockMvc.perform(
            put("/types/{typeId}", 0).contentType(ApplicationMediaType.TYPE).content("{\"name\":\"new-test-name\"}").accept(ApplicationMediaType.TYPE)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.TYPE)) //
        .andExpect(jsonPath("$.name").value("new-test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'collection')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1", "http://localhost/types/0/collections/2")));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/types/{typeId}", 0)) //
        .andExpect(status().isOk());

        verify(this.typeRepository).delete(Long.valueOf(0));
    }

}
