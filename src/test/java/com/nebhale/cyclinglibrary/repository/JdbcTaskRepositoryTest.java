
package com.nebhale.cyclinglibrary.repository;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

public final class JdbcTaskRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcTaskRepository taskRepository;

    @Test
    public void find() {
        this.jdbcTemplate.update("INSERT INTO tasks(id, status, message) VALUES(?, ?, ?)", 0, Status.IN_PROGRESS.toString(), "test-message");
        this.jdbcTemplate.update("INSERT INTO tasks(id, status, message) VALUES(?, ?, ?)", 1, Status.IN_PROGRESS.toString(), "test-message");

        Set<Task> tasks = this.taskRepository.find();

        assertEquals(2, tasks.size());
    }

    @Test
    public void create() {
        Task task = this.taskRepository.create("test-message");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, status, message FROM tasks");
        assertEquals("IN_PROGRESS", data.get("status"));
        assertEquals("test-message", data.get("message"));
        assertEquals(data.get("id"), task.getId());
        assertEquals(Status.valueOf((String) data.get("status")), task.getStatus());
        assertEquals(data.get("message"), task.getMessage());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO tasks(id, status, message) VALUES(?, ?, ?)", 0, Status.IN_PROGRESS.toString(), "test-message");

        Task task = this.taskRepository.read(Long.valueOf(0));
        assertEquals(Long.valueOf(0), task.getId());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals("test-message", task.getMessage());
    }

    @Test
    public void update() {
        this.jdbcTemplate.update("INSERT INTO tasks(id, status, message) VALUES(?, ?, ?)", 0, Status.IN_PROGRESS.toString(), "test-message");

        Task task = this.taskRepository.update(Long.valueOf(0), Status.SUCCESS, "new-test-message");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT status, message FROM tasks WHERE id = ?", 0);
        assertEquals("SUCCESS", data.get("status"));
        assertEquals("new-test-message", data.get("message"));
        assertEquals(Status.valueOf((String) data.get("status")), task.getStatus());
        assertEquals(data.get("message"), task.getMessage());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO tasks(id, status, message) VALUES(?, ?, ?)", 0, Status.IN_PROGRESS.toString(), "test-message");

        this.taskRepository.delete(Long.valueOf(0));

        assertEquals(0, countRowsInTable("tasks"));
    }
}
