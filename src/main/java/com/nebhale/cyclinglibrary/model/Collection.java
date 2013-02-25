
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
        return itemIds;
    }

    @Override
    public String toString() {
        return "Collection [typeId=" + typeId + ", name=" + name + ", itemIds=" + itemIds + ", getId()=" + getId() + "]";
    }

}
