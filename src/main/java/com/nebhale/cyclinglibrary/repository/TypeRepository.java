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
