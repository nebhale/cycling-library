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

import java.net.URI;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.jamesmurty.utils.XMLBuilder;
import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PolylineEncoder;

@Controller
@RequestMapping("/types/{typeId}/collections/{collectionId}/items/{itemId}/points")
final class PointController {

    private static final int MAX_ENCODED_LENGTH = 1850;

    private final CollectionRepository collectionRepository;

    private final ItemRepository itemRepository;

    private final PolylineEncoder polylineEncoder;

    private final String apiKey;

    @Autowired
    PointController(@Value("#{systemEnvironment.GOOGLE_API_KEY}") String apiKey, CollectionRepository collectionRepository,
        ItemRepository itemRepository, PolylineEncoder polylineEncoder) {
        Assert.hasText(apiKey, "The enviroment variable 'GOOGLE_API_KEY' must be specified");

        this.apiKey = apiKey;
        this.collectionRepository = collectionRepository;
        this.itemRepository = itemRepository;
        this.polylineEncoder = polylineEncoder;
    }

    @RequestMapping(method = RequestMethod.GET, produces = ApplicationMediaType.POINTS_JSON_VALUE)
    @ResponseBody
    List<Point> read(@PathVariable Long itemId) {
        return this.itemRepository.read(itemId).getPoints();
    }

    @RequestMapping(method = RequestMethod.GET, produces = ApplicationMediaType.POINTS_PNG_VALUE)
    @ResponseBody
    ResponseEntity<Void> read(@PathVariable Long itemId, @RequestParam(value = "maptype", defaultValue = "roadmap") String mapType,
        @RequestParam(defaultValue = "250") Integer width, @RequestParam(defaultValue = "250") Integer height,
        @Value("#{request.getRemoteAddr()}") String userIp) {

        Item item = this.itemRepository.read(itemId);

        URI uri = UriComponentsBuilder.newInstance() //
        .scheme("http") //
        .host("maps.googleapis.com") //
        .path("/maps/api/staticmap") //
        .queryParam("key", this.apiKey) //
        .queryParam("sensor", false) //
        .queryParam("userIp", userIp) //
        .queryParam("size", String.format("%dx%d", width, height)) //
        .queryParam("maptype", mapType) //
        .queryParam("scale", 2) //
        .queryParam("path", String.format("color:0xff0000ff|weight:2|%s", this.polylineEncoder.encodeSingle(MAX_ENCODED_LENGTH, item.getPoints()))) //
        .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @RequestMapping(method = RequestMethod.GET, produces = ApplicationMediaType.POINTS_XML_VALUE)
    @ResponseBody
    ResponseEntity<String> read(@PathVariable Long collectionId, @PathVariable Long itemId) throws ParserConfigurationException,
        FactoryConfigurationError, TransformerException {
        Collection collection = this.collectionRepository.read(collectionId);
        Item item = this.itemRepository.read(itemId);

        XMLBuilder trk = XMLBuilder.create("gpx") //
        .attribute("version", "1.1") //
        .attribute("creator", "gpx-library") //
        .attribute("xmlns", "http://www.topografix.com/GPX/1/1") //
        .attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance") //
        .attribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd") //
        .element("trk");

        trk.element("name").text(String.format("%s %s", collection.getShortName(), item.getShortName()));

        XMLBuilder trkseg = trk.element("trkseg");

        for (Point point : item.getPoints()) {
            trkseg.element("trkpt") //
            .attribute("lat", Double.toString(point.getLatitude())) //
            .attribute("lon", Double.toString(point.getLongitude())) //
            .element("ele") //
            .text(Double.toString(point.getElevation()));
        }

        return new ResponseEntity<String>(trk.asString(null), HttpStatus.OK);
    }
}
