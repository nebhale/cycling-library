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

package com.nebhale.cyclinglibrary.model;

import java.util.Set;

import com.nebhale.cyclinglibrary.util.Sets;

/**
 * Represents a collection in a type
 */
public final class Collection extends AbstractIdentifableSupport {

    private final Long typeId;

    private final String name;

    private final Set<Long> itemIds;

    /**
     * Creates a new instance specifying the typeId, id, and name
     * 
     * @param typeId The id of the type the collection is related to
     * @param id The id of the collection
     * @param name The name of the collection
     * @param itemIds The ids of the items related to this collection
     */
    public Collection(Long typeId, Long id, String name, Long... itemIds) {
        this(typeId, id, name, Sets.asSet(itemIds));
    }

    /**
     * Creates a new instance specifying the typeId, id, name, and item ids
     * 
     * @param typeId The id of the type the collection is related to
     * @param id The id of the collection
     * @param name The name of the collection
     * @param itemIds The ids of the items related to this collection
     */
    public Collection(Long typeId, Long id, String name, Set<Long> itemIds) {
        super(id);
        this.typeId = typeId;
        this.name = name;
        this.itemIds = itemIds;
    }

    /**
     * Returns the id of the type the collection is related to
     * 
     * @return the id of the type the collection is related to
     */
    public Long getTypeId() {
        return this.typeId;
    }

    /**
     * Returns the name of the collection
     * 
     * @return the name of the collection
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the ids of the items related to this collection
     * 
     * @return the ids of the items related to this collection
     */
    public Set<Long> getItemIds() {
        return this.itemIds;
    }

    @Override
    public String toString() {
        return "Collection [typeId=" + this.typeId + ", name=" + this.name + ", itemIds=" + this.itemIds + ", getId()=" + getId() + "]";
    }

}
