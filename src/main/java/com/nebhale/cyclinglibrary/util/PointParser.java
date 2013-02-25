
package com.nebhale.cyclinglibrary.util;

import java.util.List;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Task;

/**
 * A parser for {@link Point}s
 */
public interface PointParser {

    /**
     * Parse a string into a {@link List} of points
     * 
     * @param raw The raw data
     * @param callback The callback to be called when the points have been parsed
     * @return A task to track the parsing
     */
    Task parse(String raw, PointParserCallback callback);
}
