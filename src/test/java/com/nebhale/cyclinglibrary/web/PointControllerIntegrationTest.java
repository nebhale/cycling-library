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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PolylineEncoder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfiguration.class, IntegrationTestRepositoryConfiguration.class })
public class PointControllerIntegrationTest {

    @Autowired
    private volatile WebApplicationContext wac;

    @Autowired
    private volatile CollectionRepository collectionRepository;

    @Autowired
    private volatile ItemRepository itemRepository;

    @Autowired
    private volatile PolylineEncoder polylineEncoder;

    private volatile MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void readJson() throws Exception {
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", new Point(Double.valueOf(3),
                Double.valueOf(4), Double.valueOf(5))));

        this.mockMvc.perform(
            get("/types/{typeId}/collections/{collectionId}/items/{itemId}/points", 0, 1, 2).accept(ApplicationMediaType.POINTS_JSON)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.POINTS_JSON)) //
        .andExpect(jsonPath("$..latitude").value(Double.valueOf(3))) //
        .andExpect(jsonPath("$..longitude").value(Double.valueOf(4))) //
        .andExpect(jsonPath("$..elevation").value(Double.valueOf(5)));
    }

    @Test
    public void readImage() throws Exception {
        List<Point> points = Arrays.asList(new Point(Double.valueOf(3), Double.valueOf(4), Double.valueOf(5)));
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", points));
        when(this.polylineEncoder.encodeSingle(1850, points)).thenReturn("encoded-polyline");

        this.mockMvc.perform(
            get("/types/{typeId}/collections/{collectionId}/items/{itemId}/points?maptype=foo&width=100&height=200", 0, 1, 2).accept(
                ApplicationMediaType.POINTS_PNG)) //
        .andExpect(status().isSeeOther()) //
        .andExpect(
            header().string(
                "Location",
                "http://maps.googleapis.com/maps/api/staticmap?key=test-google-api-key&sensor=false&userIp=127.0.0.1&size=100x200&maptype=foo&scale=2&path=color:0xff0000ff%7Cweight:2%7Cencoded-polyline")) //
        .andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void readXml() throws Exception {
        when(this.collectionRepository.read(Long.valueOf(1))).thenReturn(
            new Collection(Long.valueOf(0), Long.valueOf(1), "test-name", "test-short-name", Long.valueOf(2)));
        when(this.itemRepository.read(Long.valueOf(2))).thenReturn(
            new Item(Long.valueOf(0), Long.valueOf(1), Long.valueOf(2), "test-name", "test-short-name", new Point(Double.valueOf(3),
                Double.valueOf(4), Double.valueOf(5))));

        this.mockMvc.perform(get("/types/{typeId}/collections/{collectionId}/items/{itemId}/points", 0, 1, 2).accept(ApplicationMediaType.POINTS_XML)) //
        .andExpect(status().isOk()) //
        .andExpect(content().contentType(ApplicationMediaType.POINTS_XML)) //
        .andExpect(content().string(read(new File("src/test/resources/expected-output.gpx"))));
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
