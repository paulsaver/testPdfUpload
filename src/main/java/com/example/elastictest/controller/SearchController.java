package com.example.elastictest.controller;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.repos.TagRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Controller
public class SearchController {

    private ElasticsearchOperations elasticsearchOperations;
    private TagRepository tagRepository;

    public SearchController(ElasticsearchOperations elasticsearchOperations,
                            TagRepository tagRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/search-by-text")
    public String search(Model model){

        model.addAttribute("allTags", tagRepository.findAll());
        return "search";
    }

    @PostMapping("/search-by-text")
    public String searchByText(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) List<String> customerTags,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String startDateFormatted = simpleDateFormat.format(startDate);
        String endDateFormatted = simpleDateFormat.format(endDate);

        SearchQuery searchQuery;

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("text", search));
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        if (customerTags != null && customerTags.size() != 0) {
            for (String customerTag : customerTags) {
                boolQueryBuilder.must(termQuery("customer", customerTag));
            }
        }

        boolQueryBuilder.must(rangeQuery("date").gt(startDateFormatted).lt(endDateFormatted));
        builder.withFilter(boolQueryBuilder);
        searchQuery = builder.withHighlightFields(new HighlightBuilder.Field("text").preTags("<strong>").postTags("</strong>"))
                .build();

        List<PdfDocument> searchResult = elasticsearchOperations.query(searchQuery, response -> {
            List<PdfDocument> chunk = new ArrayList<PdfDocument>();
            for (SearchHit searchHit : response.getHits()) {
                if (response.getHits().getHits().length <= 0) {
                    return null;
                }
                PdfDocument document = new PdfDocument();
                document.setId(searchHit.getId());

                document.setText(searchHit.getHighlightFields().get("text").fragments()[0].toString());
                chunk.add(document);
            }
            if (chunk.size() > 0) {
                return chunk;
            }
            return null;
        });

        model.addAttribute("files", searchResult);
        model.addAttribute("allTags", tagRepository.findAll());

        return "search";
    }
}
