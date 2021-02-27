package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import com.epam.esm.util.builder.TagLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public ResponseEntity<?> findAllTags(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                         @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        List<TagDTO> allTags = tagService.findAll(limit, page);
        for (int i = 0; i < allTags.size(); i++) {
            TagLinkBuilder builder = new TagLinkBuilder(allTags.get(i));
            builder.buildCertificatesDependsOnTagLink();
            allTags.set(i, builder.getHypermedia());
        }
        return new ResponseEntity<>(allTags, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSpecificTag(@PathVariable long id) {
        TagDTO tagToFind = tagService.find(id);
        TagLinkBuilder builder = new TagLinkBuilder(tagToFind);
        builder.buildCertificatesDependsOnTagLink();
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }


    @PostMapping("/")
    public ResponseEntity<?> addTag(@RequestBody TagDTO newTag) {
        long result = tagService.create(newTag);
        CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
        builder.buildNewTagLink(result);
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable long id) {
        tagService.delete(id);
        ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                (new ActionHypermedia("tag with id " + id + " deleted"));
        builder.buildFindAllTagsLink(Integer.parseInt(DEFAULT_LIMIT), Integer.parseInt(DEFAULT_PAGE));
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }
}
