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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

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
    public String encodeSingle(Integer maxLength, Double[][] points) {
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

        return new String[] { encodeUriComponent(encodedLatitude), encodeUriComponent(encodedLongitude) };
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

    private Double[][] filter(Integer maxLength, Double[][] points) {
        Double[][] filtered;

        if ((points.length * ENCODED_POINT_SIZE) < maxLength) {
            filtered = points;
        } else {
            int interval = points.length / (maxLength / ENCODED_POINT_SIZE);

            filtered = new Double[points.length / interval][];

            for (int i = 0; i < filtered.length; i++) {
                filtered[i] = points[i * interval];
            }
        }

        return filtered;
    }

    private boolean isLongerThan(StringBuilder builder, String[] encodedPoint, Integer maxLength) {
        return (builder.length() + encodedPoint[0].length() + encodedPoint[1].length()) > maxLength;
    }

    private String encodeUriComponent(String source) {
        try {
            byte[] bytes = encodeBytes(source.getBytes("UTF-8"));
            return new String(bytes, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] encodeBytes(byte[] source) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
        for (byte element : source) {
            int b = element;
            if (b < 0) {
                b += 256;
            }
            if (isAllowed(b)) {
                bos.write(b);
            } else {
                bos.write('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                bos.write(hex1);
                bos.write(hex2);
            }
        }
        return bos.toByteArray();
    }

    private boolean isAllowed(int c) {
        if (('=' == c) || ('+' == c) || ('&' == c)) {
            return false;
        } else {
            return isPchar(c) || ('/' == c) || ('?' == c);
        }
    }

    private boolean isAlpha(int c) {
        return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
    }

    private boolean isDigit(int c) {
        return (c >= '0') && (c <= '9');
    }

    private boolean isSubDelimiter(int c) {
        return ('!' == c) || ('$' == c) || ('&' == c) || ('\'' == c) || ('(' == c) || (')' == c) || ('*' == c) || ('+' == c) || (',' == c)
            || (';' == c) || ('=' == c);
    }

    private boolean isUnreserved(int c) {
        return isAlpha(c) || isDigit(c) || ('-' == c) || ('.' == c) || ('_' == c) || ('~' == c);
    }

    private boolean isPchar(int c) {
        return isUnreserved(c) || isSubDelimiter(c) || (':' == c) || ('@' == c);
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
