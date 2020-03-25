package com.example.elastictest.service;

import com.example.elastictest.model.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAll();

    Tag getById(Long id);

    void deleteAll();

    void deleteById(Long id);

    Tag save(Tag tag);
}
