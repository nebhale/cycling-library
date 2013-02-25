
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
        return this.pointParser.parse(itemInput.getPoints(), new CreateCallback(itemRepository, collectionId, itemInput.getName()));
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
        return this.pointParser.parse(itemInput.getPoints(), new UpdateCallback(itemRepository, itemId, itemInput.getName()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
    void delete(@PathVariable Long itemId) {
        this.itemRepository.delete(itemId);
    }

    private static final class CreateCallback implements PointParserCallback {

        private final ItemRepository itemRepository;

        private final Long collectionId;

        private final String name;

        private CreateCallback(ItemRepository itemRepository, Long collectionId, String name) {
            this.itemRepository = itemRepository;
            this.collectionId = collectionId;
            this.name = name;
        }

        @Override
        public void finished(List<Point> points) {
            this.itemRepository.create(this.collectionId, this.name, points);
        }

    }

    private static final class UpdateCallback implements PointParserCallback {

        private final ItemRepository itemRepository;

        private final Long itemId;

        private final String name;

        private UpdateCallback(ItemRepository itemRepository, Long itemId, String name) {
            this.itemRepository = itemRepository;
            this.itemId = itemId;
            this.name = name;
        }

        @Override
        public void finished(List<Point> points) {
            this.itemRepository.update(this.itemId, this.name, points);
        }

    }

}
