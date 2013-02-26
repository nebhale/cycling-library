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
