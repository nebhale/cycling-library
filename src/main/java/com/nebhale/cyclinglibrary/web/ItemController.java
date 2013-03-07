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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.model.Point;
import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.ItemRepository;
import com.nebhale.cyclinglibrary.util.PointParser;
import com.nebhale.cyclinglibrary.util.PointParserCallback;

@Controller
@RequestMapping("/types/{typeId}/collections/{collectionId}/items")
final class ItemController {

    private final ItemRepository itemRepository;

    private final PointParser pointParser;

    @Autowired
    ItemController(ItemRepository itemRepository, PointParser pointParser) {
        this.itemRepository = itemRepository;
        this.pointParser = pointParser;
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = ApplicationMediaType.ITEM_VALUE, produces = ApplicationMediaType.TASK_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    Task create(@PathVariable Long collectionId, @RequestBody ItemInput itemInput) {
        return this.pointParser.parse(itemInput.getPoints(),
            new CreateCallback(this.itemRepository, collectionId, itemInput.getName(), itemInput.getShortName()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{itemId}", produces = ApplicationMediaType.ITEM_VALUE)
    @ResponseBody
    Item read(@PathVariable Long itemId) {
        return this.itemRepository.read(itemId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{itemId}", consumes = ApplicationMediaType.ITEM_VALUE, produces = ApplicationMediaType.TASK_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    Task update(@PathVariable Long itemId, @RequestBody ItemInput itemInput) {
        return this.pointParser.parse(itemInput.getPoints(),
            new UpdateCallback(this.itemRepository, itemId, itemInput.getName(), itemInput.getShortName()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
    @ResponseBody
    void delete(@PathVariable Long itemId) {
        this.itemRepository.delete(itemId);
    }

    private static final class CreateCallback implements PointParserCallback {

        private final ItemRepository itemRepository;

        private final Long collectionId;

        private final String name;

        private final String shortName;

        private CreateCallback(ItemRepository itemRepository, Long collectionId, String name, String shortName) {
            this.itemRepository = itemRepository;
            this.collectionId = collectionId;
            this.name = name;
            this.shortName = shortName;
        }

        @Override
        public void finished(List<Point> points) {
            this.itemRepository.create(this.collectionId, this.name, this.shortName, points);
        }

    }

    private static final class UpdateCallback implements PointParserCallback {

        private final ItemRepository itemRepository;

        private final Long itemId;

        private final String name;

        private final String shortName;

        private UpdateCallback(ItemRepository itemRepository, Long itemId, String name, String shortName) {
            this.itemRepository = itemRepository;
            this.itemId = itemId;
            this.name = name;
            this.shortName = shortName;
        }

        @Override
        public void finished(List<Point> points) {
            this.itemRepository.update(this.itemId, this.name, this.shortName, points);
        }
    }

}
