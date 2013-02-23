
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

import com.nebhale.cyclinglibrary.model.Type;

@Repository
final class JdbcTypeRepository implements TypeRepository {

    private static final PreparedStatementCreatorFactory CREATE_STATEMENT = new PreparedStatementCreatorFactory("INSERT INTO types(name) VALUES(?)",
        new int[] { Types.VARCHAR });

    private static final ResultSetExtractor<Type> TYPE_EXTRACTOR = new TypeResultSetExtractor();

    private static final ResultSetExtractor<Set<Type>> SET_EXTRACTOR = new SetResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTypeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Type> find() {
        return new HashSet<Type>(this.jdbcTemplate.query(
            "SELECT types.id, types.name, collections.id FROM types LEFT OUTER JOIN collections ON types.id = collections.typeId", SET_EXTRACTOR));
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
    public Type read(Long typeId) {
        return this.jdbcTemplate.query(
            "SELECT types.id, types.name, collections.id FROM types, collections WHERE types.id = ? AND types.id = collections.typeId",
            new Object[] { typeId }, TYPE_EXTRACTOR);
    }

    @Override
    @Transactional(readOnly = false)
    public Type update(Long typeId, String name) {
        this.jdbcTemplate.update("UPDATE types SET name = ? WHERE id = ?", name, typeId);
        return read(typeId);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long typeId) {
        this.jdbcTemplate.update("DELETE FROM types WHERE id = ?", typeId);
    }

    private static final class TypeResultSetExtractor implements ResultSetExtractor<Type> {

        @Override
        public Type extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Type> set = SET_EXTRACTOR.extractData(rs);
            return set.isEmpty() ? null : set.iterator().next();
        }
    }

    private static final class SetResultSetExtractor implements ResultSetExtractor<Set<Type>> {

        @Override
        public Set<Type> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Set<Type> set = new HashSet<>();

            TypeContext context = null;

            while (rs.next()) {
                Long candidateId = rs.getLong(1);
                if (context == null) {
                    context = new TypeContext();

                    context.id = candidateId;
                    context.name = rs.getString(2);
                } else if (candidateId != context.id) {
                    set.add(context.create());
                    context = new TypeContext();

                    context.id = candidateId;
                    context.name = rs.getString(2);
                }

                Long candidateCollectionId = rs.getLong(3);
                if (!rs.wasNull()) {
                    context.collectionIds.add(candidateCollectionId);
                }
            }

            if (context != null) {
                set.add(context.create());
            }

            return set;
        }

    }

    private static final class TypeContext {

        private volatile Long id;

        private volatile String name;

        private final Set<Long> collectionIds = new HashSet<>();

        private Type create() {
            return new Type(id, name, collectionIds.toArray(new Long[collectionIds.size()]));
        }
    }

}
