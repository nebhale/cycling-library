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

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Task;

@Component
final class XmlPointParser implements PointParser {

    private static final DocumentBuilder DOCUMENT_BUILDER;
    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DOCUMENT_BUILDER = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private static final XPathExpression GPX_ROUTE;
    static {
        try {
            GPX_ROUTE = XPATH.compile("//rtept");
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final XPathExpression GPX_TRACK;
    static {
        try {
            GPX_TRACK = XPATH.compile("//trkpt");
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final XPathExpression TCX_TRACK;
    static {
        try {
            TCX_TRACK = XPATH.compile("//Position");
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final XPathExpression LATITUDE;
    static {
        try {
            LATITUDE = XPATH.compile("LatitudeDegrees/text()");
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final XPathExpression LONGITUDE;
    static {
        try {
            LONGITUDE = XPATH.compile("LongitudeDegrees/text()");
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private final PointAugmenter pointAugmenter;

    @Autowired
    XmlPointParser(PointAugmenter pointAugmenter) {
        this.pointAugmenter = pointAugmenter;
    }

    @Override
    public Task parse(String raw, PointParserCallback callback) {
        Document document = createDocument(raw);

        try {
            NodeList result;

            result = (NodeList) GPX_ROUTE.evaluate(document, XPathConstants.NODESET);
            if (result.getLength() != 0) {
                return parseGpxRoute(result, callback);
            }

            result = (NodeList) GPX_TRACK.evaluate(document, XPathConstants.NODESET);
            if (result.getLength() != 0) {
                return parseGpxTrack(result, callback);
            }

            result = (NodeList) TCX_TRACK.evaluate(document, XPathConstants.NODESET);
            if (result.getLength() != 0) {
                return parseTcxTrack(result, callback);
            }
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }

        throw new IllegalArgumentException("Unable to parse input");
    }

    private Document createDocument(String raw) {
        try {
            return DOCUMENT_BUILDER.parse(new InputSource(new StringReader(raw)));
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Task parseGpxRoute(NodeList result, PointParserCallback callback) {
        Double[][] points = new Double[result.getLength()][2];

        for (int i = 0; i < result.getLength(); i++) {
            Element element = (Element) result.item(i);

            points[i][0] = Double.parseDouble(element.getAttribute("lat"));
            points[i][1] = Double.parseDouble(element.getAttribute("lon"));
        }

        return this.pointAugmenter.augmentPoints(points, new PointAugmenterCallbackAdapter(callback));
    }

    private Task parseGpxTrack(NodeList result, PointParserCallback callback) {
        Double[][] points = new Double[result.getLength()][2];

        for (int i = 0; i < result.getLength(); i++) {
            Element element = (Element) result.item(i);

            points[i][0] = Double.parseDouble(element.getAttribute("lat"));
            points[i][1] = Double.parseDouble(element.getAttribute("lon"));
        }

        return this.pointAugmenter.augmentPoints(points, new PointAugmenterCallbackAdapter(callback));
    }

    private Task parseTcxTrack(NodeList result, PointParserCallback callback) throws XPathExpressionException {
        Double[][] points = new Double[result.getLength()][2];

        for (int i = 0; i < result.getLength(); i++) {
            Element element = (Element) result.item(i);

            points[i][0] = (Double) LATITUDE.evaluate(element, XPathConstants.NUMBER);
            points[i][1] = (Double) LONGITUDE.evaluate(element, XPathConstants.NUMBER);
        }

        return this.pointAugmenter.augmentPoints(points, new PointAugmenterCallbackAdapter(callback));
    }

    private static final class PointAugmenterCallbackAdapter implements PointAugmenterCallback {

        private final PointParserCallback delegate;

        private PointAugmenterCallbackAdapter(PointParserCallback delegate) {
            this.delegate = delegate;
        }

        @Override
        public void finished(List<Point> points) {
            this.delegate.finished(points);
        }
    }

}
