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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.util.Sets;

public final class JdbcCollectionRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcCollectionRepository collectionRepository;

    @Test
    public void create() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");

        Collection collection = this.collectionRepository.create(Long.valueOf(0), "test-name", "test-short-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, typeId, name, shortName FROM collections");
        assertEquals(Long.valueOf(0), data.get("typeId"));
        assertEquals("test-name", data.get("name"));
        assertEquals("test-short-name", data.get("shortName"));
        assertEquals(data.get("id"), collection.getId());
        assertEquals(data.get("typeId"), collection.getTypeId());
        assertEquals(data.get("name"), collection.getName());
        assertEquals(data.get("shortName"), collection.getShortName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES(?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 2, 1, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 3, 1, "test-name", "test-short-name");

        Collection collection = this.collectionRepository.read(Long.valueOf(1));
        assertEquals(Long.valueOf(1), collection.getId());
        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertEquals("test-short-name", collection.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
    }

    @Test
    public void readDoesNotExist() {
        assertNull(this.collectionRepository.read(Long.valueOf(0)));
    }

    @Test
    public void updateName() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES(?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 2, 1, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 3, 1, "test-name", "test-short-name");

        Collection collection = this.collectionRepository.update(Long.valueOf(1), "new-test-name", null);

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name, shortName FROM collections WHERE id = ?", 1);
        assertEquals("new-test-name", data.get("name"));
        assertEquals("test-short-name", data.get("shortName"));
        assertEquals(data.get("name"), collection.getName());
        assertEquals(data.get("shortName"), collection.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
    }

    @Test
    public void updateShortName() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES(?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 2, 1, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 3, 1, "test-name", "test-short-name");

        Collection collection = this.collectionRepository.update(Long.valueOf(1), null, "new-test-short-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name, shortName FROM collections WHERE id = ?", 1);
        assertEquals("test-name", data.get("name"));
        assertEquals("new-test-short-name", data.get("shortName"));
        assertEquals(data.get("name"), collection.getName());
        assertEquals(data.get("shortName"), collection.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES(?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 2, 1, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name, shortName) VALUES(?, ?, ?, ?)", 3, 1, "test-name", "test-short-name");

        this.collectionRepository.delete(Long.valueOf(1));

        assertEquals(0, countRowsInTable("collections"));
        assertEquals(0, countRowsInTable("items"));
    }
}
