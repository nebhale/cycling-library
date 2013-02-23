
package com.nebhale.cyclinglibrary.model;

/**
 * Represents a collection in a type
 */
public final class Item extends AbstractIdentifableSupport {

    private final Long typeId;

    private final Long collectionId;

    private final String name;

    /**
     * Creates a new instance specifying the typeId, collectionId, id and name
     * 
     * @param typeId The id of the type the item is related to
     * @param collectionId The id of the collection the item is related to
     * @param id The id of the item
     * @param name The name of the item
     */
    public Item(Long typeId, Long collectionId, Long id, String name) {
        super(id);
        this.typeId = typeId;
        this.collectionId = collectionId;
        this.name = name;
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

    @Override
    public String toString() {
        return "Item [typeId=" + typeId + ", collectionId=" + collectionId + ", name=" + name + ", getId()=" + getId() + "]";
    }

}
