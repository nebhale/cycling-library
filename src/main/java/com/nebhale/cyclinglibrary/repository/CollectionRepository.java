
package com.nebhale.cyclinglibrary.repository;

import com.nebhale.cyclinglibrary.model.Collection;

/**
 * A repository for accessing collections in the library
 */
public interface CollectionRepository {

    /**
     * Create a new collection
     * 
     * @param typeId The id of the type the collection is related to
     * @param name The name of the collection
     * @return The created collection
     */
    Collection create(long typeId, String name);

    /**
     * Read a collection identified by its id
     * 
     * @param collectionId The id of the collection to read
     * @return The collection
     */
    Collection read(long collectionId);

    /**
     * Update a collection identified by its id
     * 
     * @param collectionId The id of the collection to update
     * @param name The new name of the collection
     * @return The updated collection
     */
    Collection update(long collectionId, String name);

    /**
     * Delete a collection identified by its id
     * 
     * @param collectionId The id of the collection to delete
     */
    void delete(long collectionId);
}
