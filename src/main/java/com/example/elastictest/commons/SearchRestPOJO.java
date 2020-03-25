package com.example.elastictest.commons;

import java.util.List;

public class SearchRestPOJO {
    private String search;
    private List<String> customerTags;
    private String startDate;
    private String endDate;

    public SearchRestPOJO() {
    }

    public SearchRestPOJO(String search, List<String> customerTags, String startDate, String endDate) {
        this.search = search;
        this.customerTags = customerTags;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getCustomerTags() {
        return customerTags;
    }

    public void setCustomerTags(List<String> customerTags) {
        this.customerTags = customerTags;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
