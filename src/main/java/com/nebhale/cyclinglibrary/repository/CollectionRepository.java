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
     * @param shortName The short name of the collection
     * @return The created collection
     */
    Collection create(Long typeId, String name, String shortName);

    /**
     * Read a collection identified by its id
     * 
     * @param collectionId The id of the collection to read
     * @return The collection
     */
    Collection read(Long collectionId);

    /**
     * Update a collection identified by its id
     * 
     * @param collectionId The id of the collection to update
     * @param name The new name of the collection
     * @param shortName The new short name of the collection
     * @return The updated collection
     */
    Collection update(Long collectionId, String name, String shortName);

    /**
     * Delete a collection identified by its id
     * 
     * @param collectionId The id of the collection to delete
     */
    void delete(Long collectionId);
}
