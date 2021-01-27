package com.epam.esm.contoller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/")
    public ResponseEntity findAllTags() {
        List<TagDTO> allTags = null;
        try {
            allTags = tagService.findAll();
            return new ResponseEntity(allTags, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findSpecificTag(@PathVariable long id) {
        Optional<TagDTO> tagToFind = null;
        try {
            tagToFind = tagService.find(id);
            return tagToFind.map(tagDTO -> new ResponseEntity(tagDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity("not found", HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/")
    public ResponseEntity addTag(@RequestBody TagDTO newTag) {
        try {
            int result = tagService.create(newTag);
            if (result != 0) {
                return new ResponseEntity(result, HttpStatus.CREATED);
            } else {
                return new ResponseEntity("not valid data", HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTag(@PathVariable long id) {
        try {
            tagService.delete(id);
            return new ResponseEntity("tag with id " + id + " deleted", HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
