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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class GooglePolylineEncoderTest {

    private final GooglePolylineEncoder polylineEncoder = new GooglePolylineEncoder();

    @Test
    public void encode() {
        Double[][] points = new Double[][] { //
        new Double[] { 38.5, -120.2 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 43.252, -126.453 } };

        List<String> encodedPolylines = this.polylineEncoder.encode(Integer.MAX_VALUE, points);

        assertEquals(1, encodedPolylines.size());
        assertEquals("enc:_p~iF~ps|U_ulLnnqC_mqNvxq`@", encodedPolylines.get(0));
    }

    @Test
    public void encodeSplit() {
        Double[][] points = new Double[][] { //
        new Double[] { 38.5, -120.2 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 43.252, -126.453 }, //
            new Double[] { 38.5, -120.2 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 43.252, -126.453 } };

        List<String> encodedPolylines = this.polylineEncoder.encode(31, points);

        assertEquals(2, encodedPolylines.size());
        assertEquals("enc:_p~iF~ps|U_ulLnnqC_mqNvxq`@", encodedPolylines.get(0));
        assertEquals("enc:_p~iF~ps|U_ulLnnqC_mqNvxq`@", encodedPolylines.get(1));
    }

    @Test
    public void encodeSingleNoFilter() {
        Double[][] points = new Double[][] { //
        new Double[] { 38.5, -120.2 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 43.252, -126.453 } };

        String encodedPolyline = this.polylineEncoder.encodeSingle(Integer.MAX_VALUE, points);

        assertEquals("enc:_p~iF~ps|U_ulLnnqC_mqNvxq`@", encodedPolyline);
    }

    @Test
    public void encodeSingleFilter() {
        Double[][] points = new Double[][] { //
        new Double[] { 38.5, -120.2 }, //
            new Double[] { 0.0, 0.0 }, //
            new Double[] { 40.7, -120.95 }, //
            new Double[] { 0.0, 0.0 }, //
            new Double[] { 43.252, -126.453 }, //
            new Double[] { 0.0, 0.0 } };

        String encodedPolyline = this.polylineEncoder.encodeSingle(31, points);

        assertEquals("enc:_p~iF~ps|U_ulLnnqC_mqNvxq`@", encodedPolyline);
    }

}
