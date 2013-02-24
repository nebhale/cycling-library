
package com.nebhale.cyclinglibrary.repository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.nebhale.cyclinglibrary.model.Type;

@ContextConfiguration(classes = { RepositoryConfiguration.class, TestRepositoryConfiguration.class })
public final class JdbcTypeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private volatile JdbcTypeRepository typeRepository;

    @Test
    public void find() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name-1");
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 1, "test-name-2");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 2, 0, "test-name");

        Set<Type> types = this.typeRepository.find();
        assertEquals(2, types.size());
    }

    @Test
    public void create() {
        Type type = this.typeRepository.create("test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, name FROM types");
        assertEquals("test-name", data.get("name"));
        assertEquals(data.get("id"), type.getId());
        assertEquals(data.get("name"), type.getName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 2, 0, "test-name");

        Type type = this.typeRepository.read(Long.valueOf(0));
        assertEquals(Long.valueOf(0), type.getId());
        assertEquals("test-name", type.getName());
        assertArrayEquals(new Long[] { Long.valueOf(1), Long.valueOf(2) }, type.getCollectionIds());
    }

    @Test
    public void readDoesNotExist() {
        assertNull(this.typeRepository.read(Long.valueOf(0)));
    }

    @Test
    public void update() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 2, 0, "test-name");

        Type type = this.typeRepository.update(Long.valueOf(0), "new-test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM types WHERE id = ?", 0);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), type.getName());
        assertArrayEquals(new Long[] { Long.valueOf(1), Long.valueOf(2) }, type.getCollectionIds());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES (?, ?, ?)", 2, 0, "test-name");

        this.typeRepository.delete(Long.valueOf(0));

        assertEquals(0, countRowsInTable("types"));
        assertEquals(0, countRowsInTable("collections"));
    }
}
