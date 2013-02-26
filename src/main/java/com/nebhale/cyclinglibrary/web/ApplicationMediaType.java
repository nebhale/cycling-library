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

import org.springframework.http.MediaType;

/**
 * The {@link MediaType}s used by the application
 */
public final class ApplicationMediaType {

    /**
     * A String equivalent of {@link ApplicationMediaType#TYPE}.
     */
    public static final String TYPE_VALUE = "application/vnc.cycling-library.type";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.type}.
     */
    public static final MediaType TYPE = MediaType.valueOf(TYPE_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#COLLECTION}.
     */
    public static final String COLLECTION_VALUE = "application/vnc.cycling-library.collection";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.collection}.
     */
    public static final MediaType COLLECTION = MediaType.valueOf(COLLECTION_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#ITEM}.
     */
    public static final String ITEM_VALUE = "application/vnc.cycling-library.item";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.item}.
     */
    public static final MediaType ITEM = MediaType.valueOf(ITEM_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#ITEM_JSON}.
     */
    public static final String ITEM_JSON_VALUE = "application/vnc.cycling-library.item+json";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.item+json}.
     */
    public static final MediaType ITEM_JSON = MediaType.valueOf(ITEM_JSON_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#ITEM_PNG}.
     */
    public static final String ITEM_PNG_VALUE = "application/vnc.cycling-library.item+png";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.item+png}.
     */
    public static final MediaType ITEM_PNG = MediaType.valueOf(ITEM_PNG_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#ITEM_XML}.
     */
    public static final String ITEM_XML_VALUE = "application/vnc.cycling-library.item+xml";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.item+xml}.
     */
    public static final MediaType ITEM_XML = MediaType.valueOf(ITEM_XML_VALUE);

    /**
     * A String equivalent of {@link ApplicationMediaType#TASK}.
     */
    public static final String TASK_VALUE = "application/vnc.cycling-library.task";

    /**
     * Public constant media type for {@code application/vnc.cycling-library.task}.
     */
    public static final MediaType TASK = MediaType.valueOf(TASK_VALUE);

}
