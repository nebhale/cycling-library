
package com.nebhale.cyclinglibrary.util;

import java.util.List;

import com.nebhale.cyclinglibrary.model.Point;

/**
 * Callback interface for when the {@link PointParser} has completed parsing
 */
public interface PointParserCallback {

    /**
     * Called when {@link PointParser} has completed parsing
     * 
     * @param points The parsed points
     */
    void finished(List<Point> points);
}
