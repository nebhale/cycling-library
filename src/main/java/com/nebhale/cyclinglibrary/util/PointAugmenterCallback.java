
package com.nebhale.cyclinglibrary.util;

import java.util.List;

import com.nebhale.cyclinglibrary.model.Point;

/**
 * Callback interface for when the {@link PointAugmenter} has completed parsing
 */
public interface PointAugmenterCallback {

    /**
     * Called when {@link PointAugmenter} has completed parsing
     * 
     * @param points The parsed points
     */
    void finished(List<Point> points);
}
