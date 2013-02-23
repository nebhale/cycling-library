
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

import com.nebhale.cyclinglibrary.model.Item;
import com.nebhale.cyclinglibrary.repository.ItemRepository;

@Controller
@RequestMapping("/types/{typeId}/collections/{collectionId}/items")
final class ItemController {

    private final ItemRepository itemRepository;

    @Autowired
    ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Item create(@PathVariable Long collectionId, @RequestBody ItemInput itemInput) {
        return this.itemRepository.create(collectionId, itemInput.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Item read(@PathVariable Long itemId) {
        return this.itemRepository.read(itemId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Item update(@PathVariable Long itemId, @RequestBody ItemInput itemInput) {
        return this.itemRepository.update(itemId, itemInput.getName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
    void delete(@PathVariable Long itemId) {
        this.itemRepository.delete(itemId);
    }

}
