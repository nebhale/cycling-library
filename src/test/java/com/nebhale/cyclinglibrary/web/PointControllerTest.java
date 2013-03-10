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

package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PolylineEncoder;

public final class PointControllerTest {

    private final CollectionRepository collectionRepository = mock(CollectionRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final PolylineEncoder polylineEncoder = mock(PolylineEncoder.class);

    private final PointController controller = new PointController("test-google-api-key", this.collectionRepository, this.itemRepository,
        this.polylineEncoder);

    @Test
    public void readJson() {
        List<Point> points = Arrays.asList(new Point(Double.valueOf(0), Double.valueOf(1), Double.valueOf(2)));
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", points);
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(item);

        List<Point> result = this.controller.read(Long.valueOf(2));

        assertSame(points, result);
    }

    @Test
    public void readImage() {
        List<Point> points = Arrays.asList(new Point(Double.valueOf(0), Double.valueOf(1), Double.valueOf(2)));
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", points);
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(item);
        when(this.polylineEncoder.encodeSingle(1850, points)).thenReturn("encoded-polyline");

        ResponseEntity<Void> result = this.controller.read(Long.valueOf(2), "test-map-type", 100, 200, "test-user-ip");

        assertEquals(HttpStatus.SEE_OTHER, result.getStatusCode());
        assertEquals(
            URI.create("http://maps.googleapis.com/maps/api/staticmap?key=test-google-api-key&sensor=false&userIp=test-user-ip&size=100x200&maptype=test-map-type&scale=2&path=color:0xff0000ff%7Cweight:2%7Cencoded-polyline"),
            result.getHeaders().getLocation());
    }

    @Test
    public void readXml() throws ParserConfigurationException, FactoryConfigurationError, TransformerException, IOException {
        Collection collection = new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2));
        when(this.collectionRepository.read(Long.valueOf(1))).thenReturn(collection);
        List<Point> points = Arrays.asList(new Point(Double.valueOf(3), Double.valueOf(4), Double.valueOf(5)));
        Item item = new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", points);
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(item);

        ResponseEntity<String> result = this.controller.read(Long.valueOf(1), Long.valueOf(2));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(read(new File("src/test/resources/expected-output.gpx")), result.getBody());
    }

    private String read(File file) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int length;

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return out.toString("UTF-8").trim();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
