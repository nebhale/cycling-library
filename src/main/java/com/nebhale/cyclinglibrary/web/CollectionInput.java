
package com.nebhale.cyclinglibrary.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

final class CollectionInput {

    private final String name;

    @JsonCreator
    CollectionInput(@JsonProperty("name") String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CollectionInput [name=" + name + "]";
    }

}
