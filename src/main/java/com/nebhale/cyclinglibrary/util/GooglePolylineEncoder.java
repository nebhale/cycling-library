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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nebhale.cyclinglibrary.model.Point;

@Component
final class GooglePolylineEncoder implements PolylineEncoder {

    private static final int ENCODED_POINT_SIZE = 10;

    @Override
    public List<String> encode(Integer maxLength, Double[][] points) {
        List<String> encodedPolylines = new ArrayList<>();

        EncodingContext context = new EncodingContext();
        StringBuilder builder = newBuilder();

        for (Double[] point : points) {
            String[] encodedPoint = encode(point, context);

            if (isLongerThan(builder, encodedPoint, maxLength)) {
                encodedPolylines.add(builder.toString());
                builder = newBuilder();

                context.reset();

                encodedPoint = encode(point, context);
            }

            builder.append(encodedPoint[0]).append(encodedPoint[1]);
        }

        encodedPolylines.add(builder.toString());

        return encodedPolylines;
    }

    @Override
    public String encodeSingle(Integer maxLength, List<Point> points) {
        return encode(maxLength, filter(maxLength, points)).get(0);
    }

    private StringBuilder newBuilder() {
        return new StringBuilder("enc:");
    }

    private String[] encode(Double[] point, EncodingContext context) {
        int e5Latitude = (int) Math.floor(point[0] * 1e5);
        int e5Longitude = (int) Math.floor(point[1] * 1e5);

        int differenceLatitude = e5Latitude - context.previousLatitude;
        int differenceLongitude = e5Longitude - context.previousLongitude;

        context.previousLatitude = e5Latitude;
        context.previousLongitude = e5Longitude;

        String encodedLatitude = encodeSignedNumber(differenceLatitude);
        String encodedLongitude = encodeSignedNumber(differenceLongitude);

        return new String[] { encodedLatitude, encodedLongitude };
    }

    private String encodeSignedNumber(int number) {
        int signedNumber = number << 1;

        if (number < 0) {
            signedNumber = ~(signedNumber);
        }

        return encodeNumber(signedNumber);
    }

    private String encodeNumber(int number) {
        StringBuilder encoded = new StringBuilder();

        while (number >= 0x20) {
            int nextValue = (0x20 | (number & 0x1f)) + 63;
            encoded.append((char) nextValue);
            number >>= 5;
        }

        number = number + 63;
        encoded.append((char) number);

        return encoded.toString();
    }

    private Double[][] filter(Integer maxLength, List<Point> points) {
        Double[][] filtered;

        if ((points.size() * ENCODED_POINT_SIZE) < maxLength) {
            filtered = new Double[points.size()][];

            for (int i = 0; i < filtered.length; i++) {
                Point point = points.get(i);
                filtered[i] = new Double[] { point.getLatitude(), point.getLongitude() };
            }
        } else {
            int interval = points.size() / (maxLength / ENCODED_POINT_SIZE);

            filtered = new Double[points.size() / interval][];

            for (int i = 0; i < filtered.length; i++) {
                Point point = points.get(i * interval);
                filtered[i] = new Double[] { point.getLatitude(), point.getLongitude() };
            }
        }

        return filtered;
    }

    private boolean isLongerThan(StringBuilder builder, String[] encodedPoint, Integer maxLength) {
        return (builder.length() + encodedPoint[0].length() + encodedPoint[1].length()) > maxLength;
    }

    private static final class EncodingContext {

        private volatile int previousLatitude = 0;

        private volatile int previousLongitude = 0;

        private void reset() {
            this.previousLatitude = 0;
            this.previousLongitude = 0;
        }
    }

}
