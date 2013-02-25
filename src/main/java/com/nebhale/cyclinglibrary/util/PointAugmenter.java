
package com.nebhale.cyclinglibrary.util;

import com.nebhale.cyclinglibrary.model.Task;

interface PointAugmenter {

    /**
     * Augments points with their elevations
     * 
     * @param points The latitude and longitude of the point
     * @param callback The callback to be called when the points have been augmented
     * @return A task to track the augmenting
     */
    Task augmentPoints(Double[][] points, PointAugmenterCallback callback);
}
