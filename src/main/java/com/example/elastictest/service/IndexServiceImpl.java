package com.example.elastictest.service;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.model.Tag;
import com.example.elastictest.repos.TagRepository;
import com.example.elastictest.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class IndexServiceImpl implements IndexService{

    private StorageService storageService;
    private ElasticsearchOperations elasticsearchOperations;
    private PdfDocumentService pdfDocumentService;
    private TagRepository tagRepository;

    public IndexServiceImpl(StorageService storageService,
                            ElasticsearchOperations elasticsearchOperations,
                            PdfDocumentService pdfDocumentService,
                            TagRepository tagRepository) {
        this.storageService = storageService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.pdfDocumentService = pdfDocumentService;
        this.tagRepository = tagRepository;
    }

    @Override
    public PdfDocument indexDocument(MultipartFile multipartFile, String customer, Date docDate) {
        String name = storageService.store(multipartFile);

        String text;
        try {
            PDDocument document = PDDocument.load(storageService.load(name).toFile());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
            document.close();

            String[] tags = customer.split(",");
            for (String tag : tags) {
                if (!tagRepository.existsByValue(tag)) {
                    tagRepository.save(new Tag(tag));
                }
            }

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(docDate);

            PdfDocument pdfDocument = new PdfDocument(name, text, tags, date);
            pdfDocumentService.save(pdfDocument);

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(pdfDocument.getId())
                    .withObject(pdfDocument)
                    .build();
            elasticsearchOperations.index(indexQuery);

            return pdfDocument;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
