
package com.nebhale.cyclinglibrary.model;

/**
 * Represents a top-level type in the library
 */
public final class Type extends AbstractIdentifableSupport {

    private final String name;

    /**
     * Creates a new instance specifying the id and name
     * 
     * @param id The id of the type
     * @param name The name of the type
     */
    public Type(long id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * Returns the name of the type
     * 
     * @return the name of the type
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Type [name=" + name + ", getId()=" + getId() + "]";
    }

}
