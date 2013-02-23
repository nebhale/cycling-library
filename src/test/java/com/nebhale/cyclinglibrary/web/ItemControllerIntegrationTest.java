
package com.nebhale.cyclinglibrary.web;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.repository.ItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class ItemControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile ItemRepository itemRepository;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void create() throws Exception {
        when(this.itemRepository.create(Long.valueOf(1), "test-name")).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name"));

        this.mockMvc.perform(
            post("/types/{typeId}/collections/{collectionId}/items", 0, 1).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"test-name\"}").accept(
                MediaType.APPLICATION_JSON)) //
        .andExpect(status().isCreated()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1/items/2"));
        ;
    }

    @Test
    public void read() throws Exception {
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name"));

        this.mockMvc.perform(get("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2).accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1/items/2"));
    }

    @Test
    public void update() throws Exception {
        when(this.itemRepository.update(Long.valueOf(2), "new-test-name")).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "new-test-name"));

        this.mockMvc.perform(
            put("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2).contentType(MediaType.APPLICATION_JSON).content(
                "{\"name\":\"new-test-name\"}").accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("new-test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1/items/2"));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/types/{typeId}/collections/{collectionId}/items/{itemId}", 0, 1, 2)) //
        .andExpect(status().isOk());

        verify(this.itemRepository).delete(Long.valueOf(2));
    }

}
