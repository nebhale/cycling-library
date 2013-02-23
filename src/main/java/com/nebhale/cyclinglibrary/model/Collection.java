
package com.nebhale.cyclinglibrary.model;

/**
 * Represents a collection in a type
 */
public final class Collection extends AbstractIdentifableSupport {

    private final long typeId;
    
    private final String name;

    /**
     * Creates a new instance specifying the id and name
     * 
     * @param typeId The id of the type the collection is related to
     * @param id The id of the collection
     * @param name The name of the collection
     */
    public Collection(long typeId, long id, String name) {
        super(id);
        this.typeId = typeId;
        this.name = name;
    }
    
    /**
     * Returns the id of the type the collection is related to
     * 
     * @return the id of the type the collection is related to
     */
    public long getTypeId() {
        return typeId;
    }

    /**
     * Returns the name of the collection
     * 
     * @return the name of the collection
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Collection [typeId=" + typeId + ", name=" + name + ", getId()=" + getId() + "]";
    }

}
