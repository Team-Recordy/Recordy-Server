package org.recordy.server.search.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
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

    private final OpenSearchClient searchClient;

    @Override
    public void save(Search search) {
        try {
            searchClient.index(IndexRequest.of(builder ->
                    builder
                            .index(search.type().getName())
                            .id(search.id())
                            .document(search)
                            .refresh(Refresh.True))
            );
        } catch (IOException e) {
            log.error("error occurred indexing a document.");
            throw new SearchException(ErrorMessage.INDEXING_FAILED);
        }
    }

    @Override
    public List<Search> search(String query) {
        try {
            SearchRequest request = new SearchRequest.Builder()
                    .index(SearchType.PLACE.getName(), SearchType.EXHIBITION.getName())
                    .query(q -> q.queryString(qs -> qs.fields(Search.getSearchField()).query(query)))
                    .build();
            SearchResponse<Search> response = searchClient.search(request, Search.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            log.error("error occurred searching a document.");
            throw new SearchException(ErrorMessage.SEARCH_FAILED);
        }
    }
}
