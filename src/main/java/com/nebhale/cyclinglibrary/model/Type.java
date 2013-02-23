
package com.nebhale.cyclinglibrary.model;

import java.util.Arrays;

/**
 * Represents a top-level type in the library
 */
public final class Type extends AbstractIdentifableSupport {

    private final String name;

    private final Long[] collectionIds;

    /**
     * Creates a new instance specifying the id and name
     * 
     * @param id The id of the type
     * @param name The name of the type
     * @param collectionIds The ids of the collections related to this type
     */
    public Type(Long id, String name, Long... collectionIds) {
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
        return name;
    }

    /**
     * Returns the ids of the collections related to this type
     * 
     * @return the ids of the collections related to this type
     */
    public Long[] getCollectionIds() {
        return collectionIds;
    }

    @Override
    public String toString() {
        return "Type [name=" + name + ", collectionIds=" + Arrays.toString(collectionIds) + ", getId()=" + getId() + "]";
    }

}
