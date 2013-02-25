
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
        return collectionId;
    }

    /**
     * Returns the id of the item the point is related to
     * 
     * @return the id of the item the point is related to
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * Returns the latitude of the point
     * 
     * @return the latitude of the point
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * returns the longitude of the point
     * 
     * @return the longitude of the point
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Returns the elevation of the point
     * 
     * @return the elevation of the point
     */
    public Double getElevation() {
        return elevation;
    }

    @Override
    public int compareTo(Point o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Point [typeId=" + typeId + ", collectionId=" + collectionId + ", itemId=" + itemId + ", latitude=" + latitude + ", longitude="
            + longitude + ", elevation=" + elevation + ", getId()=" + getId() + "]";
    }

}
