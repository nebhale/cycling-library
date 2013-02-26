/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
