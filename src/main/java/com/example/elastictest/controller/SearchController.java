package com.example.elastictest.controller;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.repos.PdfDocumentRepository;
import com.example.elastictest.storage.StorageService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Controller
public class SearchController {

    private StorageService storageService;
    private PdfDocumentRepository pdfDocumentRepository;
    private ElasticsearchOperations elasticsearchOperations;

    public SearchController(PdfDocumentRepository pdfDocumentRepository, StorageService storageService, ElasticsearchOperations elasticsearchOperations) {
        this.pdfDocumentRepository = pdfDocumentRepository;
        this.storageService = storageService;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping("/search-by-text")
    public String search(Model model){
        return "search";
    }

    @PostMapping("/search-by-text")
    public String searchByText(Model model, @RequestParam String search){

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("text", search))
                .withHighlightFields(new HighlightBuilder.Field("text").preTags("<strong>").postTags("</strong>"))
                .build();

        List<PdfDocument> searchResult = elasticsearchOperations.query(searchQuery, response -> {
            List<PdfDocument> chunk = new ArrayList<PdfDocument>();
            for (SearchHit searchHit : response.getHits()) {
                if (response.getHits().getHits().length <= 0) {
                    return null;
                }
                PdfDocument document = new PdfDocument();
                document.setId(searchHit.getId());
                System.out.println(searchHit.getHighlightFields().get("text").fragments()[0].toString());
                document.setText(searchHit.getHighlightFields().get("text").fragments()[0].toString());
                chunk.add(document);
            }
            if (chunk.size() > 0) {
                return chunk;
            }
            return null;
        });

//        elasticsearchOperations.queryForList(searchQuery, PdfDocument.class);
//
//        List<PdfDocument> pdfDocuments = pdfDocumentRepository.findByText(search);
//        List<String> uris = new ArrayList<>();
//        for (PdfDocument pdfDocument : pdfDocuments) {
//            uris.add(storageService.load(pdfDocument.getName()).toUri().toString());
//        }
        model.addAttribute("files", searchResult);

        return "search";
    }
}
