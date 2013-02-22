
package com.nebhale.cyclinglibrary.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

final class TypeInput {

    private final String name;

    @JsonCreator
    TypeInput(@JsonProperty("name") String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TypeInput [name=" + name + "]";
    }

}
