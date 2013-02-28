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

import com.nebhale.cyclinglibrary.model.Collection;

@Repository
final class JdbcCollectionRepository implements CollectionRepository {

    private static final ResultSetExtractor<Collection> COLLECTION_EXTRACTOR = new CollectionResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    @Autowired
    JdbcCollectionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("collections").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional(readOnly = false)
    public Collection create(Long typeId, String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("typeId", typeId);
        parameters.put("name", name);

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
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
            return new Collection(this.typeId, this.id, this.name, this.itemIds);
        }
    }

}
