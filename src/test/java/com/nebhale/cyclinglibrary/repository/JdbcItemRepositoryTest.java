
package com.nebhale.cyclinglibrary.repository;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.nebhale.cyclinglibrary.model.Item;

@ContextConfiguration(classes = { RepositoryConfiguration.class, TestRepositoryConfiguration.class })
public final class JdbcItemRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private volatile JdbcItemRepository itemRepository;

    @Test
    public void create() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");

        Item item = this.itemRepository.create(Long.valueOf(1), "test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT id, collectionId, name FROM items");
        assertEquals(Long.valueOf(1), data.get("collectionId"));
        assertEquals("test-name", data.get("name"));
        assertEquals(data.get("id"), item.getId());
        assertEquals(data.get("collectionId"), item.getCollectionId());
        assertEquals(data.get("name"), item.getName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");

        Item item = this.itemRepository.read(Long.valueOf(2));
        assertEquals(Long.valueOf(2), item.getId());
        assertEquals(Long.valueOf(1), item.getCollectionId());
        assertEquals(Long.valueOf(0), item.getTypeId());
        assertEquals("test-name", item.getName());
    }

    @Test
    public void update() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");

        Item item = this.itemRepository.update(Long.valueOf(2), "new-test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM items WHERE id = ?", 2);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), item.getName());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");

        this.itemRepository.delete(Long.valueOf(2));

        assertEquals(0, countRowsInTable("items"));
    }
}
