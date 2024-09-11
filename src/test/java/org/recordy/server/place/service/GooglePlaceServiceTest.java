package org.recordy.server.place.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.recordy.server.place.domain.usecase.PlaceGoogle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class GooglePlaceServiceTest {

    @Autowired
    private GooglePlaceService googlePlaceService;

    @ValueSource(strings = {"스타벅스"})
    @ParameterizedTest
    void test(String query) {
        PlaceGoogle result = googlePlaceService.search(query);

        assertAll(
                () -> assertThat(result.placeId()).isNotNull(),
                () -> assertThat(result.geometry()).isNotNull(),
                () -> assertThat(result.formattedAddress()).isNotNull()
        );
    }
}