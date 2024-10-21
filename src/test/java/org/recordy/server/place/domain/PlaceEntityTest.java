package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceEntityTest {

    @Test
    void Place_객체로부터_PlaceEntity_객체를_생성한다() {
        // given
        Place place = PlaceFixture.create();

        // when
        PlaceEntity entity = PlaceEntity.create(place);

        // then
        assertAll(
                () -> assertThat(entity.getName()).isEqualTo(place.getName()),
                () -> assertThat(entity.getPlatformId()).isEqualTo(place.getPlatformId()),
                () -> assertThat(entity.getAddress()).isEqualTo(place.getAddress()),
                () -> assertThat(entity.getLocation().getGeometry()).isEqualTo(place.getLocation().getGeometry())
        );
    }
}