
package com.nebhale.cyclinglibrary.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

@Repository
final class JdbcTaskRepository implements TaskRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory(
        "INSERT INTO tasks(status, message) VALUES(?, ?)", new int[] { Types.VARCHAR, Types.VARCHAR });

    private static final RowMapper<Task> TASK_MAPPER = new TaskRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTaskRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional
    public Set<Task> find() {
        return new HashSet<>(this.jdbcTemplate.query("SELECT id,  status, message FROM tasks", TASK_MAPPER));
    }

    @Override
    @Transactional(readOnly = false)
    public Task create(String format, Object... arguments) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(
            CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { Status.IN_PROGRESS.toString(), String.format(format, arguments) }), keyHolder);

        return read(keyHolder.getKey().longValue());
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
