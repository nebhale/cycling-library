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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.StubPointFactory;

public final class JdbcItemRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcItemRepository itemRepository;

    @Test
    public void create() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");

        Item item = this.itemRepository.create(Long.valueOf(1), "test-name", StubPointFactory.create(0), StubPointFactory.create(1));

        Map<String, Object> itemData = this.jdbcTemplate.queryForMap("SELECT id, collectionId, name FROM items");
        assertEquals(Long.valueOf(1), itemData.get("collectionId"));
        assertEquals("test-name", itemData.get("name"));
        assertEquals(itemData.get("id"), item.getId());
        assertEquals(itemData.get("collectionId"), item.getCollectionId());
        assertEquals(itemData.get("name"), item.getName());

        List<Point> points = item.getPoints();
        List<Map<String, Object>> pointsData = this.jdbcTemplate.queryForList("SELECT id, itemId, latitude, longitude, elevation FROM points");

        Point point0 = points.get(0);
        Map<String, Object> point0Data = pointsData.get(0);
        assertEquals(item.getId(), point0Data.get("itemId"));
        assertEquals(Double.valueOf(0), point0Data.get("latitude"));
        assertEquals(Double.valueOf(1), point0Data.get("longitude"));
        assertEquals(Double.valueOf(2), point0Data.get("elevation"));
        assertEquals(point0Data.get("id"), point0.getId());
        assertEquals(point0Data.get("itemId"), point0.getItemId());
        assertEquals(point0Data.get("latitude"), point0.getLatitude());
        assertEquals(point0Data.get("longitude"), point0.getLongitude());
        assertEquals(point0Data.get("elevation"), point0.getElevation());

        Point point1 = points.get(1);
        Map<String, Object> point1Data = pointsData.get(1);
        assertEquals(item.getId(), point1Data.get("itemId"));
        assertEquals(Double.valueOf(1), point1Data.get("latitude"));
        assertEquals(Double.valueOf(2), point1Data.get("longitude"));
        assertEquals(Double.valueOf(3), point1Data.get("elevation"));
        assertEquals(point1Data.get("id"), point1.getId());
        assertEquals(point1Data.get("itemId"), point1.getItemId());
        assertEquals(point1Data.get("latitude"), point1.getLatitude());
        assertEquals(point1Data.get("longitude"), point1.getLongitude());
        assertEquals(point1Data.get("elevation"), point1.getElevation());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 3, 2, 4, 5, 6);
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 7, 2, 8, 9, 10);

        Item item = this.itemRepository.read(Long.valueOf(2));
        assertEquals(Long.valueOf(2), item.getId());
        assertEquals(Long.valueOf(1), item.getCollectionId());
        assertEquals(Long.valueOf(0), item.getTypeId());
        assertEquals("test-name", item.getName());

        List<Point> points = item.getPoints();

        Point point0 = points.get(0);
        assertEquals(Long.valueOf(3), point0.getId());
        assertEquals(Long.valueOf(2), point0.getItemId());
        assertEquals(Long.valueOf(1), point0.getCollectionId());
        assertEquals(Long.valueOf(0), point0.getTypeId());
        assertEquals(Double.valueOf(4), point0.getLatitude());
        assertEquals(Double.valueOf(5), point0.getLongitude());
        assertEquals(Double.valueOf(6), point0.getElevation());

        Point point1 = points.get(1);
        assertEquals(Long.valueOf(7), point1.getId());
        assertEquals(Long.valueOf(2), point1.getItemId());
        assertEquals(Long.valueOf(1), point1.getCollectionId());
        assertEquals(Long.valueOf(0), point1.getTypeId());
        assertEquals(Double.valueOf(8), point1.getLatitude());
        assertEquals(Double.valueOf(9), point1.getLongitude());
        assertEquals(Double.valueOf(10), point1.getElevation());
    }

    @Test
    public void readNoPoints() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");

        Item item = this.itemRepository.read(Long.valueOf(2));
        assertTrue(item.getPoints().isEmpty());
    }

    @Test
    public void readDoesNotExist() {
        assertNull(this.itemRepository.read(Long.valueOf(0)));
    }

    @Test
    public void updateName() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 3, 2, 4, 5, 6);
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 7, 2, 8, 9, 10);

        Item item = this.itemRepository.update(Long.valueOf(2), "new-test-name");

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM items WHERE id = ?", 2);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), item.getName());
        assertEquals(Long.valueOf(3), item.getPoints().get(0).getId());
        assertEquals(Long.valueOf(7), item.getPoints().get(1).getId());
    }

    @Test
    public void updateEmptyPoints() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 3, 2, 4, 5, 6);
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 7, 2, 8, 9, 10);

        Item item = this.itemRepository.update(Long.valueOf(2), "new-test-name", (List<Point>) null);

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM items WHERE id = ?", 2);
        assertEquals("new-test-name", data.get("name"));
        assertEquals(data.get("name"), item.getName());
        assertEquals(Long.valueOf(3), item.getPoints().get(0).getId());
        assertEquals(Long.valueOf(7), item.getPoints().get(1).getId());
    }

    @Test
    public void updatePoints() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 3, 2, 4, 5, 6);
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 7, 2, 8, 9, 10);

        Item item = this.itemRepository.update(Long.valueOf(2), null, StubPointFactory.create(0), StubPointFactory.create(1));

        Map<String, Object> data = this.jdbcTemplate.queryForMap("SELECT name FROM items WHERE id = ?", 2);
        assertEquals("test-name", data.get("name"));
        assertEquals(data.get("name"), item.getName());
        assertNotEquals(Long.valueOf(3), item.getPoints().get(0).getId());
        assertNotEquals(Long.valueOf(7), item.getPoints().get(1).getId());
    }

    @Test
    public void delete() {
        this.jdbcTemplate.update("INSERT INTO types(id, name) VALUES(?, ?)", 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO collections(id, typeId, name) VALUES(?, ?, ?)", 1, 0, "test-name");
        this.jdbcTemplate.update("INSERT INTO items(id, collectionId, name) VALUES(?, ?, ?)", 2, 1, "test-name");
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 3, 2, 4, 5, 6);
        this.jdbcTemplate.update("INSERT INTO points(id, itemId, latitude, longitude, elevation) VALUES(?, ?, ?, ?, ?)", 7, 2, 8, 9, 10);

        this.itemRepository.delete(Long.valueOf(2));

        assertEquals(0, countRowsInTable("items"));
        assertEquals(0, countRowsInTable("points"));
    }
}
