package com.example.elastictest.commons;

public class SearchResultPOJO {
    private String docName;
    private String highlight;

    public SearchResultPOJO() {
    }

    public SearchResultPOJO(String docName, String highlight) {
        this.docName = docName;
        this.highlight = highlight;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }
}
