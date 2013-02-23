
package com.nebhale.cyclinglibrary.repository;

import static org.junit.Assert.assertEquals;

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
        this.jdbcTemplate.update("INSERT INTO types(name) VALUES(?)", "test-name-1");
        this.jdbcTemplate.update("INSERT INTO types(name) VALUES(?)", "test-name-2");

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

        Type type = this.typeRepository.read(0);
        assertEquals(0, type.getId());
        assertEquals("test-name", type.getName());
    }

    @Test
    public void update() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");

        Type type = this.typeRepository.update(0, "new-test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM types WHERE id = ?", 0);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), type.getName());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");

        this.typeRepository.delete(0);

        assertEquals(0, countRowsInTable("types"));
    }
}
