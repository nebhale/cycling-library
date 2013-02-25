
package com.nebhale.cyclinglibrary.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for dealing with {@link Set}s
 */
public final class Sets {

    /**
     * Creates a {@link Set} containing specified items
     * 
     * @param items The items to put in the created set
     * @return The created {@link Set}
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... items) {
        Set<T> set = new HashSet<>();

        for (T item : items) {
            set.add(item);
        }

        return set;
    }
}
