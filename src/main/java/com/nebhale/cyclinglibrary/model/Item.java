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

import java.util.Arrays;
import java.util.List;

/**
 * Represents an item in a collection
 */
public final class Item extends AbstractIdentifableSupport {

    private final Long typeId;

    private final Long collectionId;

    private final String name;

    private final String shortName;

    private final List<Point> points;

    /**
     * Creates a new instance specifying the typeId, collectionId, id, name, and points
     * 
     * @param typeId The id of the type the item is related to
     * @param collectionId The id of the collection the item is related to
     * @param id The id of the item
     * @param name The name of the item
     * @param shortName The short name of the type
     * @param points The points of the item
     */
    public Item(Long typeId, Long collectionId, Long id, String name, String shortName, Point... points) {
        this(typeId, collectionId, id, name, shortName, Arrays.asList(points));
    }

    /**
     * Creates a new instance specifying the typeId, collectionId, id, name, and points
     * 
     * @param typeId The id of the type the item is related to
     * @param collectionId The id of the collection the item is related to
     * @param id The id of the item
     * @param name The name of the item
     * @param shortName The short name of the type
     * @param points The points of the item
     */
    public Item(Long typeId, Long collectionId, Long id, String name, String shortName, List<Point> points) {
        super(id);
        this.typeId = typeId;
        this.collectionId = collectionId;
        this.name = name;
        this.shortName = shortName;
        this.points = points;
    }

    /**
     * Returns the id of the type the item is related to
     * 
     * @return the id of the type the item is related to
     */
    public Long getTypeId() {
        return this.typeId;
    }

    /**
     * Returns the id of the collection the item is related to
     * 
     * @return the id of the collection the item is related to
     */
    public Long getCollectionId() {
        return this.collectionId;
    }

    /**
     * Returns the name of the item
     * 
     * @return the name of the item
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the short name of the type
     * 
     * @return the short name of the type
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Returns the points of the item
     * 
     * @return the points of the item
     */
    public List<Point> getPoints() {
        return this.points;
    }

    @Override
    public String toString() {
        return "Item [typeId=" + this.typeId + ", collectionId=" + this.collectionId + ", name=" + this.name + ", shortName=" + this.shortName
            + ", points=" + this.points + ", getId()=" + getId() + "]";
    }

}
