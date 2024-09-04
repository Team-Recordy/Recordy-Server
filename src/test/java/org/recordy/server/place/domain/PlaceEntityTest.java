package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.PlaceFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceEntityTest {

    @Test
    void Place_객체로부터_PlaceEntity_객체를_생성한다() {
        // given
        long id = 1L;
        List<Exhibition> exhibitions = List.of(ExhibitionFixture.create(1), ExhibitionFixture.create(2));
        Place place = PlaceFixture.create(id, exhibitions);

        // when
        PlaceEntity entity = PlaceEntity.from(place);

        // then
        assertAll(
                () -> assertThat(entity.getId()).isEqualTo(id),
                () -> assertThat(entity.getName()).isEqualTo(PlaceFixture.NAME),
                () -> assertThat(entity.getExhibitions().get(0).getId()).isEqualTo(1L),
                () -> assertThat(entity.getExhibitions().get(1).getId()).isEqualTo(2L),
                () -> assertThat(entity.getLocation().getId()).isEqualTo(PlaceFixture.LOCATION.getId())
        );
    }
}