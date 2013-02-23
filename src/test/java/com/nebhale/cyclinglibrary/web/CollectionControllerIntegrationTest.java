
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
        when(this.collectionRepository.create(Long.valueOf(0), "test-name")).thenReturn(new Collection(Long.valueOf(0), Long.valueOf(1), "test-name"));

        this.mockMvc.perform(
            post("/types/{typeId}/collections", 0).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"test-name\"}").accept(
                MediaType.APPLICATION_JSON)) //
        .andExpect(status().isCreated()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1"));
        ;
    }

    @Test
    public void read() throws Exception {
        when(this.collectionRepository.read(Long.valueOf(1))).thenReturn(new Collection(Long.valueOf(0), Long.valueOf(1), "test-name"));

        this.mockMvc.perform(get("/types/{typeId}/collections/{collectionId}", 0, 1).accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1"));
    }

    @Test
    public void update() throws Exception {
        when(this.collectionRepository.update(Long.valueOf(1), "new-test-name")).thenReturn(
            new Collection(Long.valueOf(0), Long.valueOf(1), "new-test-name"));

        this.mockMvc.perform(
            put("/types/{typeId}/collections/{collectionId}", 0, 1).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"new-test-name\"}").accept(
                MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("new-test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0/collections/1"));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/types/{typeId}/collections/{collectionId}", 0, 1)) //
        .andExpect(status().isOk());

        verify(this.collectionRepository).delete(Long.valueOf(1));
    }

}
