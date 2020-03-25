package com.example.elastictest.service;

import com.example.elastictest.model.PdfDocument;

import java.util.List;

public interface PdfDocumentService {

    List<PdfDocument> getAll();

    PdfDocument getById(String id);

    void deleteById(String id);

    void deleteAll();

    PdfDocument create(PdfDocument pdfDocument);

    PdfDocument save(PdfDocument pdfDocument);
}
