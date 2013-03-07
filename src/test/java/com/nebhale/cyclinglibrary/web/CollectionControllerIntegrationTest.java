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

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class CollectionControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile CollectionRepository collectionRepository;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void create() throws Exception {
        when(this.collectionRepository.create(Long.valueOf(0), "test-name", "test-short-name")).thenReturn(
            new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2), Long.valueOf(3)));

        this.mockMvc.perform(
            post("/types/{typeId}/collections", 0).contentType(ApplicationMediaType.COLLECTION).content(
                "{\"name\":\"test-name\",\"shortName\":\"test-short-name\"}").accept(ApplicationMediaType.COLLECTION)) //
        .andExpect(status().isCreated()) //
        .andExpect(content().contentType(ApplicationMediaType.COLLECTION)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.shortName").value("test-short-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'create-item')].href").value("http://localhost/types/0/collections/1/items")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'item')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1/items/2", "http://localhost/types/0/collections/1/items/3")));
    }

    @Test
    public void read() throws Exception {
        when(this.collectionRepository.read(Long.valueOf(1))).thenReturn(
            new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2), Long.valueOf(3)));

        this.mockMvc.perform(get("/types/{typeId}/collections/{collectionId}", 0, 1).accept(ApplicationMediaType.COLLECTION)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.COLLECTION)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.shortName").value("test-short-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'create-item')].href").value("http://localhost/types/0/collections/1/items")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'item')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1/items/2", "http://localhost/types/0/collections/1/items/3")));
    }

    @Test
    public void update() throws Exception {
        when(this.collectionRepository.update(Long.valueOf(1), "new-test-name", "new-test-short-name")).thenReturn(
            new Collection(Long.valueOf(0), Long.valueOf(1), "new-test-name", "new-test-short-name", Long.valueOf(2), Long.valueOf(3)));

        this.mockMvc.perform(
            put("/types/{typeId}/collections/{collectionId}", 0, 1).contentType(ApplicationMediaType.COLLECTION).content(
                "{\"name\":\"new-test-name\",\"shortName\":\"new-test-short-name\"}").accept(ApplicationMediaType.COLLECTION)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.COLLECTION)) //
        .andExpect(jsonPath("$.name").value("new-test-name")) //
        .andExpect(jsonPath("$.shortName").value("new-test-short-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'create-item')].href").value("http://localhost/types/0/collections/1/items")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'item')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1/items/2", "http://localhost/types/0/collections/1/items/3")));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/types/{typeId}/collections/{collectionId}", 0, 1)) //
        .andExpect(status().isOk());

        verify(this.collectionRepository).delete(Long.valueOf(1));
    }

}
