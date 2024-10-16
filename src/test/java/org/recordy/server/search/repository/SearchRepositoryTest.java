package org.recordy.server.search.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch.core.DeleteByQueryRequest;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.domain.SearchType;
import org.recordy.server.util.PlaceFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchRepositoryTest {

    @Autowired
    ExhibitionRepository exhibitionRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ExhibitionService exhibitionService;

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

    @Test
    void 전시_객체를_저장하면_인덱싱된다() {
        // given
        String name = "데이비드 호크니 전시";
        Place place = placeRepository.save(PlaceFixture.create());

        // when
        exhibitionService.create(new ExhibitionCreateRequest(
                name,
                LocalDate.now(),
                LocalDate.now(),
                true,
                place.getId()
        ));

        // then
        List<Search> result = searchRepository.search(name.split(" ")[0]);
        assertThat(result.size()).isEqualTo(1);
    }
}
