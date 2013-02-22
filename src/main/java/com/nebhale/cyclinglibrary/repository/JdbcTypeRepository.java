
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

import com.nebhale.cyclinglibrary.model.Type;

@Repository
final class JdbcTypeRepository implements TypeRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory("INSERT INTO types(name) VALUES(?)",
        new int[] { Types.VARCHAR });

    private static final RowMapper<Type> ROW_MAPPER = new TypeRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTypeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Type> find() {
        return new HashSet<Type>(this.jdbcTemplate.query("SELECT id, name FROM types", ROW_MAPPER));
    }

    @Override
    @Transactional(readOnly = false)
    public Type create(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { name }), keyHolder);

        return new Type(keyHolder.getKey().longValue(), name);
    }

    @Override
    @Transactional(readOnly = true)
    public Type read(long typeId) {
        return this.jdbcTemplate.queryForObject("SELECT id, name FROM types WHERE id = ?", new Object[] { typeId }, ROW_MAPPER);
    }

    @Override
    @Transactional(readOnly = false)
    public Type update(long typeId, String name) {
        this.jdbcTemplate.update("UPDATE types SET name = ? WHERE id = ?", name, typeId);
        return new Type(typeId, name);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(long typeId) {
        this.jdbcTemplate.update("DELETE FROM types WHERE id = ?", typeId);
    }

    private static final class TypeRowMapper implements RowMapper<Type> {

        @Override
        public Type mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Type(rs.getLong(1), rs.getString(2));
        }

    }

}
