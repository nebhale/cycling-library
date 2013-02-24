
package com.nebhale.cyclinglibrary.repository;

import com.nebhale.cyclinglibrary.model.Item;

/**
 * A repository for accessing items in the library
 */
public interface ItemRepository {

    /**
     * Create a new item
     * 
     * @param collectionId The id of the collection the item is related to
     * @param name The name of the item
     * @return The created item
     */
    Item create(Long collectionId, String name);

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
     * @return The updated item
     */
    Item update(Long itemId, String name);

    /**
     * Delete am item identified by its id
     * 
     * @param itemId The id of the item to delete
     */
    void delete(Long itemId);
}
