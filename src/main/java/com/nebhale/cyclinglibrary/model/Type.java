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
 * Represents a top-level type in the library
 */
public final class Type extends AbstractIdentifableSupport {

    private final String name;

    private final Set<Long> collectionIds;

    /**
     * Creates a new instance specifying the id, name, and collection ids
     * 
     * @param id The id of the type
     * @param name The name of the type
     * @param collectionIds The ids of the collections related to this type
     */
    public Type(Long id, String name, Long... collectionIds) {
        this(id, name, Sets.asSet(collectionIds));
    }

    /**
     * Creates a new instance specifying the id, name, and collection ids
     * 
     * @param id The id of the type
     * @param name The name of the type
     * @param collectionIds The ids of the collections related to this type
     */
    public Type(Long id, String name, Set<Long> collectionIds) {
        super(id);
        this.name = name;
        this.collectionIds = collectionIds;
    }

    /**
     * Returns the name of the type
     * 
     * @return the name of the type
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the ids of the collections related to this type
     * 
     * @return the ids of the collections related to this type
     */
    public Set<Long> getCollectionIds() {
        return this.collectionIds;
    }

    @Override
    public String toString() {
        return "Type [name=" + this.name + ", collectionIds=" + this.collectionIds + ", getId()=" + getId() + "]";
    }

}
