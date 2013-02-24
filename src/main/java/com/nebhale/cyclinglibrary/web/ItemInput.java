
package com.nebhale.cyclinglibrary.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

final class ItemInput {

    private final String name;

    @JsonCreator
    ItemInput(@JsonProperty("name") String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ItemInput [name=" + name + "]";
    }

}
