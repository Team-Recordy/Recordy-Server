package org.recordy.server.search.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.domain.SearchType;
import org.recordy.server.search.exception.SearchException;
import org.recordy.server.search.repository.SearchRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SearchRepositoryImpl implements SearchRepository {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void save(Search search) {
        try {
            elasticsearchClient.index(i -> i
                    .index(search.type().name())
                    .id(search.getId())
                    .document(search)
            );
        } catch (IOException e) {
            log.error("error occurred indexing a document.");
            throw new SearchException(ErrorMessage.SEARCH_FAILED);
        }
    }

    @Override
    public List<Search> search(String query) {
        try {
            SearchResponse<Search> response = elasticsearchClient.search(new SearchRequest.Builder()
                            .index(SearchType.PLACE.name(), SearchType.EXHIBITION.name())
                            .query(q -> q
                                    .match(m -> m
                                            .field(Search.getSearchField())
                                            .query(query)
                                    )
                            )
                            .build(),
                    Search.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            log.error("error occurred searching on index.");
            throw new SearchException(ErrorMessage.SEARCH_FAILED);
        }
    }
}
