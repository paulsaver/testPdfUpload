package com.example.elastictest.commons;

public class FileResponse {
    private String name;
    private String uri;
    private String type;
    private String text;
    private String docId;
    private long size;

    public FileResponse(String name, String uri, String type, String text, String docId, long size) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.text = text;
        this.docId = docId;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileResponse{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}
