
package com.nebhale.cyclinglibrary.web;

import java.util.Set;

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

    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Set<Type> find() {
        return this.typeRepository.find();
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Type create(@RequestBody TypeInput typeInput) {
        return this.typeRepository.create(typeInput.getName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{typeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Type read(@PathVariable Long typeId) {
        return this.typeRepository.read(typeId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{typeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Type update(@PathVariable Long typeId, @RequestBody TypeInput typeInput) {
        return this.typeRepository.update(typeId, typeInput.getName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{typeId}")
    void delete(@PathVariable Long typeId) {
        this.typeRepository.delete(typeId);
    }

}
