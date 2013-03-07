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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nebhale.cyclinglibrary.model.Collection;
import com.nebhale.cyclinglibrary.repository.CollectionRepository;

@Controller
@RequestMapping("/types/{typeId}/collections")
final class CollectionController {

    private final CollectionRepository collectionRepository;

    @Autowired
    CollectionController(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = ApplicationMediaType.COLLECTION_VALUE, produces = ApplicationMediaType.COLLECTION_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Collection create(@PathVariable Long typeId, @RequestBody CollectionInput collectionInput) {
        return this.collectionRepository.create(typeId, collectionInput.getName(), collectionInput.getShortName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{collectionId}", produces = ApplicationMediaType.COLLECTION_VALUE)
    @ResponseBody
    Collection read(@PathVariable Long collectionId) {
        return this.collectionRepository.read(collectionId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{collectionId}", consumes = ApplicationMediaType.COLLECTION_VALUE, produces = ApplicationMediaType.COLLECTION_VALUE)
    @ResponseBody
    Collection update(@PathVariable Long collectionId, @RequestBody CollectionInput collectionInput) {
        return this.collectionRepository.update(collectionId, collectionInput.getName(), collectionInput.getShortName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{collectionId}")
    @ResponseBody
    void delete(@PathVariable Long collectionId) {
        this.collectionRepository.delete(collectionId);
    }

}
