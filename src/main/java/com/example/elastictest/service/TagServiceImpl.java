package com.example.elastictest.service;

import com.example.elastictest.model.Tag;
import com.example.elastictest.repos.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAll() {
        tagRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.findById(tag.getId())
                .map(foundTag -> {
                    foundTag.setValue(tag.getValue());
                    return tagRepository.save(foundTag);
                })
                .orElseGet(() -> tagRepository.save(tag));
    }
}
