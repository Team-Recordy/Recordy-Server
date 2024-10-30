package org.recordy.server.place.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlatformPlaceServiceTest {

    @Autowired
    private PlatformPlaceService platformPlaceService;

    @ValueSource(strings = {"스타벅스"})
    @ParameterizedTest
    void 검색_결과중_가장_정확도가_높은_장소에_대한_정보를_수집할_수_있다(String query) {
        // when
        List<PlatformPlaceSearchResponse> result = platformPlaceService.search(query, 0);

        // then
        assertThat(result).isNotEmpty();
    }
}