
package com.nebhale.cyclinglibrary.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

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

import com.nebhale.cyclinglibrary.model.Collection;

@Repository
final class JdbcCollectionRepository implements CollectionRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory(
        "INSERT INTO collections(typeId, name) VALUES(?, ?)", new int[] { Types.BIGINT, Types.VARCHAR });

    private static final ResultSetExtractor<Collection> COLLECTION_EXTRACTOR = new CollectionResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcCollectionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = false)
    public Collection create(Long typeId, String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(CREATE_STATEMENT.newPreparedStatementCreator(new Object[] { typeId, name }), keyHolder);

        return read(keyHolder.getKey().longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection read(Long collectionId) {
        return this.jdbcTemplate.query(
            "SELECT types.id, collections.id, collections.name, items.id FROM types, collections LEFT OUTER JOIN items ON collections.id = items.collectionId WHERE collections.id = ? AND collections.typeId = types.id",
            new Object[] { collectionId }, COLLECTION_EXTRACTOR);
    }

    @Override
    @Transactional(readOnly = false)
    public Collection update(Long collectionId, String name) {
        this.jdbcTemplate.update("UPDATE collections SET name = ? WHERE id = ?", name, collectionId);
        return read(collectionId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long collectionId) {
        this.jdbcTemplate.update("DELETE FROM collections WHERE id = ?", collectionId);
    }

    private static final class CollectionResultSetExtractor implements ResultSetExtractor<Collection> {

        @Override
        public Collection extractData(ResultSet rs) throws SQLException, DataAccessException {
            CollectionContext context = null;

            while (rs.next()) {
                if (context == null) {
                    context = new CollectionContext();

                    context.typeId = rs.getLong(1);
                    context.id = rs.getLong(2);
                    context.name = rs.getString(3);
                }

                Long candidateItemId = rs.getLong(4);
                if (!rs.wasNull()) {
                    context.itemIds.add(candidateItemId);
                }
            }

            return context == null ? null : context.create();
        }

    }

    private static final class CollectionContext {

        private volatile Long typeId;

        private volatile Long id;

        private volatile String name;

        private final Set<Long> itemIds = new HashSet<>();

        private Collection create() {
            return new Collection(typeId, id, name, itemIds);
        }
    }

}
