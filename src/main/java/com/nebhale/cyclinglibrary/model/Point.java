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

/**
 * Represents a point in an item
 */
public final class Point extends AbstractIdentifableSupport implements Comparable<Point> {

    private final Long typeId;

    private final Long collectionId;

    private final Long itemId;

    private final Double latitude;

    private final Double longitude;

    private final Double elevation;

    /**
     * Creates a new instance specifying the latitude, longitude, and elevation
     * 
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @param elevation The elevation of the point
     */
    public Point(Double latitude, Double longitude, Double elevation) {
        this(null, null, null, null, latitude, longitude, elevation);
    }

    /**
     * Creates a new instance specifying the typeId, collectionId, itemId, id, latitude, longitude, and elevation
     * 
     * @param typeId The id of the type the point is related to
     * @param collectionId The id of the collection the point is related to
     * @param itemId The id of the item the point is related to;
     * @param id The id of the point
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @param elevation The elevation of the point
     */
    public Point(Long typeId, Long collectionId, Long itemId, Long id, Double latitude, Double longitude, Double elevation) {
        super(id);
        this.typeId = typeId;
        this.collectionId = collectionId;
        this.itemId = itemId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    /**
     * Returns the id of the type the point is related to
     * 
     * @return the id of the type the point is related to
     */
    public Long getTypeId() {
        return this.typeId;
    }

    /**
     * Returns the id of the collection the point is related to
     * 
     * @return the id of the collection the point is related to
     */
    public Long getCollectionId() {
        return this.collectionId;
    }

    /**
     * Returns the id of the item the point is related to
     * 
     * @return the id of the item the point is related to
     */
    public Long getItemId() {
        return this.itemId;
    }

    /**
     * Returns the latitude of the point
     * 
     * @return the latitude of the point
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * returns the longitude of the point
     * 
     * @return the longitude of the point
     */
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * Returns the elevation of the point
     * 
     * @return the elevation of the point
     */
    public Double getElevation() {
        return this.elevation;
    }

    @Override
    public int compareTo(Point o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Point [typeId=" + this.typeId + ", collectionId=" + this.collectionId + ", itemId=" + this.itemId + ", latitude=" + this.latitude
            + ", longitude=" + this.longitude + ", elevation=" + this.elevation + ", getId()=" + getId() + "]";
    }

}
