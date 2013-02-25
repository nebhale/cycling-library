
package com.nebhale.cyclinglibrary.web;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class TaskControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile TaskRepository taskRepository;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void find() throws Exception {
        Set<Task> tasks = new HashSet<>();
        tasks.add(new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message"));
        when(this.taskRepository.find()).thenReturn(tasks);

        this.mockMvc.perform(get("/tasks").accept(ApplicationMediaType.TASK)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.TASK)) //
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void read() throws Exception {
        when(this.taskRepository.read(Long.valueOf(0))).thenReturn(new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message"));

        this.mockMvc.perform(get("/tasks/{taskId}", 0).accept(ApplicationMediaType.TASK)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.TASK)) //
        .andExpect(jsonPath("$.status").value("IN_PROGRESS")) //
        .andExpect(jsonPath("$.message").value("test-message")) //
        .andExpect(jsonPath("$.links[?(@.rel== 'self')].href").value("http://localhost/tasks/0"));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{taskId}", 0)) //
        .andExpect(status().isOk());

        verify(this.taskRepository).delete(Long.valueOf(0));
    }

}
