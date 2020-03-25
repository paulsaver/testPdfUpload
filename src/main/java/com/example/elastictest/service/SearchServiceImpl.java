package com.example.elastictest.service;

import com.example.elastictest.commons.SearchRestPOJO;
import com.example.elastictest.commons.SearchResultPOJO;
import com.example.elastictest.repos.TagRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

@Service
public class SearchServiceImpl implements SearchService {

    private ElasticsearchOperations elasticsearchOperations;
    private TagRepository tagRepository;

    public SearchServiceImpl(ElasticsearchOperations elasticsearchOperations, TagRepository tagRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<SearchResultPOJO> searchByTextAndCustomerAndStartDateAndEndDate(SearchRestPOJO searchRestPOJO) {
        SearchQuery searchQuery;

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("text", searchRestPOJO.getSearch() == null ? "" : searchRestPOJO.getSearch()));
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        if (searchRestPOJO.getCustomerTags() != null && searchRestPOJO.getCustomerTags().size() != 0) {
            for (String customerTag : searchRestPOJO.getCustomerTags()) {
                boolQueryBuilder.must(termQuery("customer", customerTag));
            }
        }

        boolQueryBuilder.must(rangeQuery("date").gt(searchRestPOJO.getStartDate()).lt(searchRestPOJO.getEndDate()));
        builder.withFilter(boolQueryBuilder);
        searchQuery = builder.withHighlightFields(new HighlightBuilder.Field("text").preTags("<strong>").postTags("</strong>"))
                .build();

        return elasticsearchOperations.query(searchQuery, response -> {
            List<SearchResultPOJO> chunk = new ArrayList<>();
            for (SearchHit searchHit : response.getHits()) {
                if (response.getHits().getHits().length <= 0) {
                    return null;
                }

                chunk.add(new SearchResultPOJO(searchHit.getId(), searchHit.getHighlightFields().get("text").fragments()[0].toString()));
            }
            if (chunk.size() > 0) {
                return chunk;
            }
            return null;
        });
    }
}
