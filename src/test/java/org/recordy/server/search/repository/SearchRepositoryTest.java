package org.recordy.server.search.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch.core.DeleteByQueryRequest;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.domain.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchRepositoryTest {

    @Autowired
    OpenSearchClient searchClient;

    @Autowired
    SearchRepository searchRepository;

    @AfterEach
    void close() {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest.Builder()
                    .index(SearchType.PLACE.getName())
                    .query(QueryBuilders.matchAll().build()._toQuery())
                    .build();

            searchClient.deleteByQuery(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 검색_문서를_인덱싱할_수_있다() throws Exception {
        // given
        Search search = new Search("1:PLACE", SearchType.PLACE, "서울시 마포구 독막로 209", "우리집");

        // when
        searchRepository.save(search);

        // then
        assertThat(searchClient.search(s -> s.index(search.type().getName()), Search.class).hits().hits().size()).isEqualTo(1);
    }

    @ValueSource(strings = {"서울", "시립", "미술"})
    @ParameterizedTest
    void 검색어를_통해_문서를_검색할_수_있다(String query) {
        // given
        Search search = new Search("1:PLACE", SearchType.PLACE, "서울시 마포구 독막로 209", "서울시립미술관");
        searchRepository.save(search);

        // when
        List<Search> result = searchRepository.search(query);

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}
