
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
import org.springframework.http.MediaType;
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

        this.mockMvc.perform(get("/types").accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create() throws Exception {
        when(this.typeRepository.create("test-name")).thenReturn(new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2)));

        this.mockMvc.perform(
            post("/types").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"test-name\"}").accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isCreated()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
        .andExpect(jsonPath("$.name").value("test-name")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/types/0")) //
        .andExpect(
            jsonPath("$.links[?(@.rel== 'collection')].href").value(
                Arrays.asList("http://localhost/types/0/collections/1", "http://localhost/types/0/collections/2")));
    }

    @Test
    public void read() throws Exception {
        when(this.typeRepository.read(Long.valueOf(0))).thenReturn(new Type(Long.valueOf(0), "test-name", Long.valueOf(1), Long.valueOf(2)));

        this.mockMvc.perform(get("/types/{typeId}", 0).accept(MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
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
            put("/types/{typeId}", 0).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"new-test-name\"}").accept(
                MediaType.APPLICATION_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
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
