
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
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");

        Collection collection = this.collectionRepository.create(Long.valueOf(0), "test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, typeId, name FROM collections");
        assertEquals(Long.valueOf(0), data.get("typeId"));
        assertEquals("test-name", data.get("name"));
        assertEquals(data.get("id"), collection.getId());
        assertEquals(data.get("typeId"), collection.getTypeId());
        assertEquals(data.get("name"), collection.getName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 3, 1, "test-name");

        Collection collection = this.collectionRepository.read(Long.valueOf(1));
        assertEquals(Long.valueOf(1), collection.getId());
        assertEquals(Long.valueOf(0), collection.getTypeId());
        assertEquals("test-name", collection.getName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
    }

    @Test
    public void readDoesNotExist() {
        assertNull(this.collectionRepository.read(Long.valueOf(0)));
    }

    @Test
    public void update() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 3, 1, "test-name");

        Collection collection = this.collectionRepository.update(Long.valueOf(1), "new-test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM collections WHERE id = ?", 1);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), collection.getName());
        assertEquals(Sets.asSet(Long.valueOf(2), Long.valueOf(3)), collection.getItemIds());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 3, 1, "test-name");

        this.collectionRepository.delete(Long.valueOf(1));

        assertEquals(0, countRowsInTable("collections"));
        assertEquals(0, countRowsInTable("items"));
    }
}
