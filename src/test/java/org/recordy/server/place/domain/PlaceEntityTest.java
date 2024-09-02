package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.PlaceFixture;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceEntityTest {

    @Test
    void Place_객체로부터_PlaceEntity_객체를_생성한다() {
        // given
        long id = 1L;
        Place place = PlaceFixture.create(id);

        // when
        PlaceEntity entity = PlaceEntity.from(place);

        // then
        assertThat(entity.getId()).isEqualTo(id);
    }
}