
package com.nebhale.cyclinglibrary.util;

import java.util.List;

/**
 * A utility that encodes points into a polyline format
 */
public interface PolylineEncoder {

    /**
     * Encodes the points into a list of encoded polylines. The number of polylines is governed by the maximum length of
     * any single encoded polyline.
     * 
     * @param maxLength The maximum length of an encoded polyline
     * @param points The points to encode into polylines
     * @return The list of encoded polylines
     */
    List<String> encode(Integer maxLength, Double[][] points);

    /**
     * Encodes the points into a single encoded polyline. The resolution of the data is lowered to ensure that the
     * resulting polyline is smaller than the maximum length.
     * 
     * @param maxLength The maximum length of the encoded polyline
     * @param points The points to encode into a polyline
     * @return The encoded polyline
     */
    String encodeSingle(Integer maxLength, Double[][] points);

}
