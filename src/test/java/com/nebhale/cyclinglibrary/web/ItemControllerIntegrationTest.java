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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PointParser;
import com.nebhale.cyclinglibrary.util.PointParserCallback;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class ItemControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile ItemRepository itemRepository;

    @Autowired
    private volatile PointParser pointParser;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void create() throws Exception {
        Task task = new Task(Long.valueOf(4), Status.IN_PROGRESS, "test-message");
        when(this.pointParser.parse(eq("test-points"), any(PointParserCallback.class))).thenReturn(task);

        this.mockMvc.perform(
            post("/types/{typeId}/collections/{collectionId}/items", 0, 1).contentType(ApplicationMediaType.ITEM).content(
                "{\"name\":\"test-name\", \"points\":\"test-points\"}").accept(ApplicationMediaType.TASK)) //
        .andExpect(status().isAccepted()) //
        .andExpect(content().contentType(ApplicationMediaType.TASK)) //
        .andExpect(jsonPath("$.status").value("IN_PROGRESS")) //
        .andExpect(jsonPath("$.message").value("test-message")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/tasks/4"));
    }

    @Test
    public void read() throws Exception {
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name"));

        this.mockMvc.perform(get("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2).accept(ApplicationMediaType.ITEM)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.ITEM)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.shortName").value("test-short-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1/items/2"));
    }

    @Test
    public void update() throws Exception {
        Task task = new Task(Long.valueOf(4), Status.IN_PROGRESS, "test-message");
        when(this.pointParser.parse(eq("new-test-points"), any(PointParserCallback.class))).thenReturn(task);

        this.mockMvc.perform(
            put("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2).contentType(ApplicationMediaType.ITEM).content(
                "{\"name\":\"new-test-name\", \"points\":\"new-test-points\"}").accept(ApplicationMediaType.TASK)) //
        .andExpect(status().isAccepted()) //
        .andExpect(content().contentType(ApplicationMediaType.TASK)) //
        .andExpect(jsonPath("$.status").value("IN_PROGRESS")) //
        .andExpect(jsonPath("$.message").value("test-message")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/tasks/4"));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2)) //
        .andExpect(status().isOk());

        verify(this.itemRepository).delete(Long.valueOf(2));
    }

}
