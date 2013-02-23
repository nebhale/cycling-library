
package com.nebhale.cyclinglibrary.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nebhale.cyclinglibrary.model.Collection;

@Repository
final class JdbcCollectionRepository implements CollectionRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory(
        "INSERT INTO collections(typeId, name) VALUES(?, ?)", new int[] { Types.BIGINT, Types.VARCHAR });

    private static final RowMapper<Collection> ROW_MAPPER = new CollectionRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcCollectionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = false)
    public Collection create(long typeId, String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { typeId, name }), keyHolder);

        return new Collection(typeId, keyHolder.getKey().longValue(), name);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection read(long collectionId) {
        return this.jdbcTemplate.queryForObject(
            "SELECT types.id, collections.id, collections.name FROM types, collections WHERE collections.id = ? AND collections.typeId = types.id",
            new Object[] { collectionId }, ROW_MAPPER);
    }

    @Override
    @Transactional(readOnly = false)
    public Collection update(long collectionId, String name) {
        this.jdbcTemplate.update("UPDATE collections SET name = ? WHERE id = ?", name, collectionId);
        return read(collectionId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(long collectionId) {
        this.jdbcTemplate.update("DELETE FROM collections WHERE id = ?", collectionId);
    }

    private static final class CollectionRowMapper implements RowMapper<Collection> {

        @Override
        public Collection mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Collection(rs.getLong(1), rs.getLong(2), rs.getString(3));
        }

    }

}
