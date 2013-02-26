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

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nebhale.cyclinglibrary.model.Type;
import com.nebhale.cyclinglibrary.repository.TypeRepository;

@Controller
@RequestMapping("/types")
final class TypeController {

    private final TypeRepository typeRepository;

    @Autowired
    TypeController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "", produces = ApplicationMediaType.TYPE_VALUE)
    @ResponseBody
    Set<Type> find() {
        return this.typeRepository.find();
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = ApplicationMediaType.TYPE_VALUE, produces = ApplicationMediaType.TYPE_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Type create(@RequestBody TypeInput typeInput) {
        return this.typeRepository.create(typeInput.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{typeId}", produces = ApplicationMediaType.TYPE_VALUE)
    @ResponseBody
    Type read(@PathVariable Long typeId) {
        return this.typeRepository.read(typeId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{typeId}", consumes = ApplicationMediaType.TYPE_VALUE, produces = ApplicationMediaType.TYPE_VALUE)
    @ResponseBody
    Type update(@PathVariable Long typeId, @RequestBody TypeInput typeInput) {
        return this.typeRepository.update(typeId, typeInput.getName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{typeId}")
    void delete(@PathVariable Long typeId) {
        this.typeRepository.delete(typeId);
    }

}
