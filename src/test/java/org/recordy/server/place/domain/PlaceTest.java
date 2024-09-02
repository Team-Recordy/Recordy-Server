package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.util.PlaceFixture;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceTest {

    @Test
    void Place_엔티티_객체로부터_Place_객체를_생성한다() {
        // given
        long id = 1L;
        PlaceEntity entity = PlaceEntity.from(PlaceFixture.create(id));

        // when
        Place place = Place.from(entity);

        // then
        assertThat(place.getId()).isEqualTo(id);
    }

    @Test
    void PlaceCreate_객체로부터_Place_객체를_생성한다() {
        // given
        long id = 1L;
        PlaceCreate create = new PlaceCreate(id);

        // when
        Place place = Place.create(create);

        // then
        assertThat(place.getId()).isEqualTo(id);
    }
}