
package com.nebhale.cyclinglibrary.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Collection create(@PathVariable long typeId, @RequestBody CollectionInput collectionInput) {
        return this.collectionRepository.create(typeId, collectionInput.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Collection read(@PathVariable long collectionId) {
        return this.collectionRepository.read(collectionId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{collectionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Collection update(@PathVariable long collectionId, @RequestBody CollectionInput collectionInput) {
        return this.collectionRepository.update(collectionId, collectionInput.getName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{collectionId}")
    void delete(@PathVariable long collectionId) {
        this.collectionRepository.delete(collectionId);
    }

}
