package com.example.elastictest.repos;

import com.example.elastictest.model.PdfDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PdfDocumentRepository extends ElasticsearchRepository<PdfDocument, Long> {

    List<PdfDocument> findByText(String text);

    List<PdfDocument> findByName(String name);
}
