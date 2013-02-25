
package com.nebhale.cyclinglibrary.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;

@Repository
final class JdbcItemRepository implements ItemRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory(
        "INSERT INTO items(collectionId, name) VALUES(?, ?)", new int[] { Types.BIGINT, Types.VARCHAR });

    private static final ResultSetExtractor<Item> ITEM_EXTRACTOR = new ItemResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcItemRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = false)
    public Item create(Long collectionId, String name, Point... points) {
        return create(collectionId, name, Arrays.asList(points));
    }

    @Override
    @Transactional(readOnly = false)
    public Item create(Long collectionId, String name, List<Point> points) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { collectionId, name }), keyHolder);
        Long itemId = keyHolder.getKey().longValue();
        insertPoints(itemId, points);

        return read(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public Item read(Long itemId) {
        return this.jdbcTemplate.query(
            "SELECT types.id, collections.id, items.id, items.name, points.id, points.latitude, points.longitude, points.elevation FROM types, collections, items LEFT OUTER JOIN points ON items.id = points.itemId WHERE items.id = ? AND items.collectionId = collections.id AND collections.typeId = types.id",
            new Object[] { itemId }, ITEM_EXTRACTOR);
    }

    @Override
    @Transactional(readOnly = false)
    public Item update(Long itemId, String name, Point... points) {
        return update(itemId, name, Arrays.asList(points));
    }

    @Override
    @Transactional(readOnly = false)
    public Item update(Long itemId, String name, List<Point> points) {
        if (name != null) {
            this.jdbcTemplate.update("UPDATE items SET name = ? WHERE id = ?", name, itemId);
        }

        if (points != null && !points.isEmpty()) {
            this.jdbcTemplate.update("DELETE FROM points WHERE itemId = ?", itemId);
            insertPoints(itemId, points);
        }

        return read(itemId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long itemId) {
        this.jdbcTemplate.update("DELETE FROM items WHERE id = ?", itemId);
    }

    private void insertPoints(Long itemId, List<Point> points) {
        for (Point point : points) {
            this.jdbcTemplate.update("INSERT INTO points(itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?)",
                new Object[] { itemId, point.getLatitude(), point.getLongitude(), point.getElevation() });
        }
    }

    private static final class ItemResultSetExtractor implements ResultSetExtractor<Item> {

        @Override
        public Item extractData(ResultSet rs) throws SQLException, DataAccessException {
            ItemContext context = null;

            while (rs.next()) {
                if (context == null) {
                    context = new ItemContext();

                    context.typeId = rs.getLong(1);
                    context.collectionId = rs.getLong(2);
                    context.id = rs.getLong(3);
                    context.name = rs.getString(4);
                }

                Long candidatePointId = rs.getLong(5);
                if (!rs.wasNull()) {
                    context.points.add(new Point(context.typeId, context.collectionId, context.id, candidatePointId, rs.getDouble(6), rs.getDouble(7),
                        rs.getDouble(8)));
                }
            }

            return context == null ? null : context.create();
        }
    }

    private static final class ItemContext {

        private volatile Long typeId;

        private volatile Long collectionId;

        private volatile Long id;

        private volatile String name;

        private final List<Point> points = new ArrayList<>();

        private Item create() {
            Collections.sort(points);
            return new Item(typeId, collectionId, id, name, points);
        }
    }

}
