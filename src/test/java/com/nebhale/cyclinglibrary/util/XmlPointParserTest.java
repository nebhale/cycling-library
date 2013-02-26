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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Status;
import com.nebhale.cyclinglibrary.model.Task;

public class XmlPointParserTest {

    private final ArgumentCaptor<Double[][]> pointsArgumentCaptor = ArgumentCaptor.forClass(Double[][].class);

    private final ArgumentCaptor<PointAugmenterCallback> callbackArgumentCaptor = ArgumentCaptor.forClass(PointAugmenterCallback.class);

    private final StubPointParserCallback callback = new StubPointParserCallback();

    private final Task task = new Task(Long.valueOf(0), Status.IN_PROGRESS, "test-message");

    private final PointAugmenter pointAugmenter = mock(PointAugmenter.class);

    private final XmlPointParser pointParser = new XmlPointParser(this.pointAugmenter);

    @Before
    public void setupCaptor() {
        when(this.pointAugmenter.augmentPoints(this.pointsArgumentCaptor.capture(), this.callbackArgumentCaptor.capture())).thenReturn(this.task);
    }

    @Test
    public void parseGpxRoute() throws IOException {
        test("src/test/resources/gpx-route.gpx");
    }

    @Test
    public void parseGpxTrack() throws IOException {
        test("src/test/resources/gpx-track.gpx");
    }

    @Test
    public void parseTcxTrack() throws IOException {
        test("src/test/resources/tcx-track.tcx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseUnsupported() throws IOException {
        test("src/test/resources/test.xml");
    }

    private void test(String filename) throws IOException {
        String content = loadFile(filename);
        Task result = this.pointParser.parse(content, this.callback);

        assertEquals(this.task, result);

        this.callbackArgumentCaptor.getValue().finished(null);
        assertTrue(this.callback.called);

        Double[][] points = this.pointsArgumentCaptor.getValue();
        assertEquals(Double.valueOf(50.855030), points[0][0]);
        assertEquals(Double.valueOf(-1.503780), points[0][1]);
        assertEquals(Double.valueOf(50.857760), points[1][0]);
        assertEquals(Double.valueOf(-1.510630), points[1][1]);
        assertEquals(Double.valueOf(50.858240), points[2][0]);
        assertEquals(Double.valueOf(-1.512010), points[2][1]);
    }

    private String loadFile(String filename) throws IOException {
        Reader in = null;
        Writer out = null;

        try {
            in = new FileReader(filename);
            out = new StringWriter();

            char[] buffer = new char[8192];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            return out.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Nothing to do here
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Nothing to do here
                }
            }
        }
    }

    private static final class StubPointParserCallback implements PointParserCallback {

        private volatile boolean called = false;

        @Override
        public void finished(List<Point> points) {
            this.called = true;
        }

    }

}
