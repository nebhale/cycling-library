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

import java.util.List;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;

/**
 * A repository for accessing items in the library
 */
public interface ItemRepository {

    /**
     * Create a new item
     * 
     * @param collectionId The id of the collection the item is related to
     * @param name The name of the item
     * @param points The points of the item
     * @param shortName The short name of the item
     * @return The created item
     */
    Item create(Long collectionId, String name, String shortName, Point... points);

    /**
     * Create a new item
     * 
     * @param collectionId The id of the collection the item is related to
     * @param name The name of the item
     * @param shortName The short name of the item
     * @param points The points of the item
     * @return The created item
     */
    Item create(Long collectionId, String name, String shortName, List<Point> points);

    /**
     * Read an item identified by its id
     * 
     * @param itemId The id of the item to read
     * @return The item
     */
    Item read(Long itemId);

    /**
     * Update an item identified by its id
     * 
     * @param itemId The id of the item to update
     * @param name The new name of the item
     * @param shortName The new short name of the item
     * @param points The new points of the item
     * @return The updated item
     */
    Item update(Long itemId, String name, String shortName, Point... points);

    /**
     * Update an item identified by its id
     * 
     * @param itemId The id of the item to update
     * @param name The new name of the item
     * @param shortName The new short name of the item
     * @param points The new points of the item
     * @return The updated item
     */
    Item update(Long itemId, String name, String shortName, List<Point> points);

    /**
     * Delete am item identified by its id
     * 
     * @param itemId The id of the item to delete
     */
    void delete(Long itemId);
}
