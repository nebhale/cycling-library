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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nebhale.cyclinglibrary.model.Type;

@Repository
final class JdbcTypeRepository implements TypeRepository {

    private static final ResultSetExtractor<Type> TYPE_EXTRACTOR = new TypeResultSetExtractor();

    private static final ResultSetExtractor<Set<Type>> SET_EXTRACTOR = new SetResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    @Autowired
    JdbcTypeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("types").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Type> find() {
        return new HashSet<Type>(this.jdbcTemplate.query(
            "SELECT types.id, types.name, types.shortName, collections.id FROM types LEFT OUTER JOIN collections ON types.id = collections.typeId",
            SET_EXTRACTOR));
    }

    @Override
    @Transactional(readOnly = false)
    public Type create(String name, String shortName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("shortName", shortName);

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Type read(Long typeId) {
        return this.jdbcTemplate.query(
            "SELECT types.id, types.name, types.shortName, collections.id FROM types LEFT OUTER JOIN collections ON types.id = collections.typeId WHERE types.id = ?",
            new Object[] { typeId }, TYPE_EXTRACTOR);
    }

    @Override
    @Transactional(readOnly = false)
    public Type update(Long typeId, String name, String shortName) {
        if (name != null) {
            this.jdbcTemplate.update("UPDATE types SET name = ? WHERE id = ?", name, typeId);
        }

        if (shortName != null) {
            this.jdbcTemplate.update("UPDATE types SET shortName = ? WHERE id = ?", shortName, typeId);
        }

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
                    context.shortName = rs.getString(3);
                } else if (candidateId != context.id) {
                    set.add(context.create());
                    context = new TypeContext();

                    context.id = candidateId;
                    context.name = rs.getString(2);
                    context.shortName = rs.getString(3);
                }

                Long candidateCollectionId = rs.getLong(4);
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

        private volatile String shortName;

        private final Set<Long> collectionIds = new HashSet<>();

        private Type create() {
            return new Type(this.id, this.name, this.shortName, this.collectionIds);
        }
    }

}
