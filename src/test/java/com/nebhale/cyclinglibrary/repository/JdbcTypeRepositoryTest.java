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
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nebhale.cyclinglibrary.model.Type;
import com.nebhale.cyclinglibrary.util.Sets;

public final class JdbcTypeRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcTypeRepository typeRepository;

    @Test
    public void find() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name-1", "test-short-name-1");
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 1, "test-name-2", "test-short-name-2");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 2, 0, "test-name", "test-short-name");

        Set<Type> types = this.typeRepository.find();
        assertEquals(2, types.size());
    }

    @Test
    public void create() {
        Type type = this.typeRepository.create("test-name", "test-short-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, name, shortName FROM types");
        assertEquals("test-name", data.get("name"));
        assertEquals("test-short-name", data.get("shortName"));
        assertEquals(data.get("id"), type.getId());
        assertEquals(data.get("name"), type.getName());
        assertEquals(data.get("shortName"), type.getShortName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 2, 0, "test-name", "test-short-name");

        Type type = this.typeRepository.read(Long.valueOf(0));
        assertEquals(Long.valueOf(0), type.getId());
        assertEquals("test-name", type.getName());
        assertEquals("test-short-name", type.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(1), Long.valueOf(2)), type.getCollectionIds());
    }

    @Test
    public void readDoesNotExist() {
        assertNull(this.typeRepository.read(Long.valueOf(0)));
    }

    @Test
    public void updateName() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 2, 0, "test-name", "test-short-name");

        Type type = this.typeRepository.update(Long.valueOf(0), "new-test-name", null);

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name, shortName FROM types WHERE id = ?", 0);
        assertEquals("new-test-name", data.get("name"));
        assertEquals("test-short-name", data.get("shortName"));
        assertEquals(data.get("name"), type.getName());
        assertEquals(data.get("shortName"), type.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(1), Long.valueOf(2)), type.getCollectionIds());
    }

    @Test
    public void updateShortName() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 2, 0, "test-name", "test-short-name");

        Type type = this.typeRepository.update(Long.valueOf(0), null, "new-test-short-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name, shortName FROM types WHERE id = ?", 0);
        assertEquals("test-name", data.get("name"));
        assertEquals("new-test-short-name", data.get("shortName"));
        assertEquals(data.get("name"), type.getName());
        assertEquals(data.get("shortName"), type.getShortName());
        assertEquals(Sets.asSet(Long.valueOf(1), Long.valueOf(2)), type.getCollectionIds());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name, shortName) VALUES(?, ?, ?)", 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 1, 0, "test-name", "test-short-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name, shortName) VALUES (?, ?, ?, ?)", 2, 0, "test-name", "test-short-name");

        this.typeRepository.delete(Long.valueOf(0));

        assertEquals(0, countRowsInTable("types"));
        assertEquals(0, countRowsInTable("collections"));
    }
}
