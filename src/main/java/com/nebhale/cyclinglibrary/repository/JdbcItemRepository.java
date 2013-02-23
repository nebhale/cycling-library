
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

import com.nebhale.cyclinglibrary.model.Item;

@Repository
final class JdbcItemRepository implements ItemRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory(
        "INSERT INTO items(collectionId, name) VALUES(?, ?)", new int[] { Types.BIGINT, Types.VARCHAR });

    private static final RowMapper<Item> ROW_MAPPER = new ItemRowMapper();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcItemRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = false)
    public Item create(Long collectionId, String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { collectionId, name }), keyHolder);

        return read(keyHolder.getKey().longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Item read(Long itemId) {
        return this.jdbcTemplate.queryForObject(
            "SELECT types.id, collections.id, items.id, items.name FROM types, collections, items WHERE items.id = ? AND items.collectionId = collections.id AND collections.typeId = types.id",
            new Object[] { itemId }, ROW_MAPPER);
    }

    @Override
    @Transactional(readOnly = false)
    public Item update(Long itemId, String name) {
        this.jdbcTemplate.update("UPDATE items SET name = ? WHERE id = ?", name, itemId);
        return read(itemId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long itemId) {
        this.jdbcTemplate.update("DELETE FROM items WHERE id = ?", itemId);
    }

    private static final class ItemRowMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Item(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4));
        }

    }

}
