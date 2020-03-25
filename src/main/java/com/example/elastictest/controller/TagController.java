package com.example.elastictest.controller;

import com.example.elastictest.model.Tag;
import com.example.elastictest.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/all")
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @DeleteMapping("/all")
    public void deleteAllTags() {
        tagService.deleteAll();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id){
        return tagService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTagById(@PathVariable Long id) {
        tagService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Tag updateTag(@RequestBody Tag tag, @PathVariable Long id) {
        tag.setId(id);
        return tagService.save(tag);
    }
}
