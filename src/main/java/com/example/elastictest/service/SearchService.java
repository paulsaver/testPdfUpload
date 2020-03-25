package com.example.elastictest.service;

import com.example.elastictest.commons.SearchRestPOJO;
import com.example.elastictest.commons.SearchResultPOJO;

import java.util.List;

public interface SearchService {

    List<SearchResultPOJO> searchByTextAndCustomerAndStartDateAndEndDate(SearchRestPOJO searchRestPOJO);
}
