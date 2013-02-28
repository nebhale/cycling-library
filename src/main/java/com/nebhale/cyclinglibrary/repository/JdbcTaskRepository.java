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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

@Repository
final class JdbcTaskRepository implements TaskRepository {

    private static final RowMapper<Task> TASK_MAPPER = new TaskRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    @Autowired
    JdbcTaskRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("tasks").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional
    public Set<Task> find() {
        return new HashSet<>(this.jdbcTemplate.query("SELECT id,  status, message FROM tasks", TASK_MAPPER));
    }

    @Override
    @Transactional(readOnly = false)
    public Task create(String format, Object... arguments) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", Status.IN_PROGRESS.toString());
        parameters.put("message", String.format(format, arguments));

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Task read(Long taskId) {
        return this.jdbcTemplate.queryForObject("SELECT id, status, message FROM tasks WHERE id = ?", new Object[] { taskId }, TASK_MAPPER);
    }

    @Override
    @Transactional(readOnly = false)
    public Task update(Long taskId, Status status, String format, Object... arguments) {
        this.jdbcTemplate.update("UPDATE tasks SET status = ?, message = ? WHERE id = ?", status.toString(), String.format(format, arguments), taskId);
        return read(taskId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long taskId) {
        this.jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", taskId);
    }

    private static final class TaskRowMapper implements RowMapper<Task> {

        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(rs.getLong(1), Status.valueOf(rs.getString(2)), rs.getString(3));
        }

    }

}
