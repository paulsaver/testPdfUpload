package com.example.elastictest.controller;

import com.example.elastictest.commons.SearchRestPOJO;
import com.example.elastictest.commons.SearchResultPOJO;
import com.example.elastictest.service.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public List<SearchResultPOJO> restSearchByText(@RequestBody SearchRestPOJO searchRestPOJO) {

        return searchService.searchByTextAndCustomerAndStartDateAndEndDate(searchRestPOJO);
    }
}
