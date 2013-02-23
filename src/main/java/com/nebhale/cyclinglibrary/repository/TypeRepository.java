
package com.nebhale.cyclinglibrary.repository;

import java.util.Set;

import com.nebhale.cyclinglibrary.model.Type;

/**
 * A repository for accessing top-level types in the library
 */
public interface TypeRepository {

    /**
     * Find all types
     * 
     * @return All types
     */
    Set<Type> find();

    /**
     * Create a new type
     * 
     * @param name The name of the type
     * @return The created type
     */
    Type create(String name);

    /**
     * Read a type identified by its id
     * 
     * @param typeId The id of the type to read
     * @return The type
     */
    Type read(Long typeId);

    /**
     * Update a type identified by its id
     * 
     * @param typeId The id of the type to update
     * @param name The new name of the type
     * @return The updated type
     */
    Type update(Long typeId, String name);

    /**
     * Delete a type identified by its id
     * 
     * @param typeId The id of the type to delete
     */
    void delete(Long typeId);
}
