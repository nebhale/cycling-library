
package com.nebhale.cyclinglibrary.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

final class ItemInput {

    private final String name;

    private final String points;

    @JsonCreator
    ItemInput(@JsonProperty("name") String name, @JsonProperty("points") String points) {
        this.name = name;
        this.points = points;
    }

    String getName() {
        return name;
    }

    String getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "ItemInput [name=" + name + ", points=" + points + "]";
    }

}
