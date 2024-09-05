package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceTest {

    @Test
    void Place_엔티티_객체로부터_Place_객체를_생성한다() {
        // given
        long id = 1L;
        List<Exhibition> exhibitions = List.of(ExhibitionFixture.create(1), ExhibitionFixture.create(2));
        PlaceEntity entity = PlaceEntity.from(PlaceFixture.create(id, exhibitions));

        // when
        Place place = Place.from(entity);

        // then
        assertAll(
                () -> assertThat(place.getId()).isEqualTo(id),
                () -> assertThat(place.getName()).isEqualTo(PlaceFixture.NAME),
                () -> assertThat(place.getExhibitions().get(0).getId()).isEqualTo(1L),
                () -> assertThat(place.getExhibitions().get(1).getId()).isEqualTo(2L),
                () -> assertThat(place.getLocation().getAddress().getFormatted()).isEqualTo(LocationFixture.FORMATTED)
        );
    }

    @Test
    void PlaceCreate_객체로부터_Place_객체를_생성한다() {
        // given
        long id = 1L;
        PlaceCreate create = new PlaceCreate(id, PlaceFixture.NAME, LocationFixture.create());

        // when
        Place place = Place.create(create);

        // then
        assertAll(
                () -> assertThat(place.getId()).isEqualTo(id),
                () -> assertThat(place.getName()).isEqualTo(PlaceFixture.NAME),
                () -> assertThat(place.getExhibitions()).isEmpty(),
                () -> assertThat(place.getLocation()).isNotNull()
        );
    }
}