
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

    private final List<Point> points;

    /**
     * Creates a new instance specifying the typeId, collectionId, id, name, and points
     * 
     * @param typeId The id of the type the item is related to
     * @param collectionId The id of the collection the item is related to
     * @param id The id of the item
     * @param name The name of the item
     * @param points The points of the item
     */
    public Item(Long typeId, Long collectionId, Long id, String name, Point... points) {
        this(typeId, collectionId, id, name, Arrays.asList(points));
    }

    /**
     * Creates a new instance specifying the typeId, collectionId, id, name, and points
     * 
     * @param typeId The id of the type the item is related to
     * @param collectionId The id of the collection the item is related to
     * @param id The id of the item
     * @param name The name of the item
     * @param points The points of the item
     */
    public Item(Long typeId, Long collectionId, Long id, String name, List<Point> points) {
        super(id);
        this.typeId = typeId;
        this.collectionId = collectionId;
        this.name = name;
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
        return collectionId;
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
     * Returns the points of the item
     * 
     * @return the points of the item
     */
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Item [typeId=" + typeId + ", collectionId=" + collectionId + ", name=" + name + ", points=" + points + ", getId()=" + getId() + "]";
    }

}
