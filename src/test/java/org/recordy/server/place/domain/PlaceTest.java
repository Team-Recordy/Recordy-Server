package org.recordy.server.place.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceTest {

    @Test
    void Place_엔티티_객체로부터_Place_객체를_생성한다() {
        // given
        PlaceEntity entity = PlaceEntity.create(PlaceFixture.create());

        // when
        Place place = Place.from(entity);

        // then
        assertAll(
                () -> assertThat(place.getName()).isEqualTo(entity.getName()),
                () -> assertThat(place.getPlatformId()).isEqualTo(entity.getPlatformId()),
                () -> assertThat(place.getAddress()).isEqualTo(entity.getAddress()),
                () -> assertThat(place.getLocation().getGeometry()).isEqualTo(entity.getLocation().getGeometry())
        );
    }

    @Test
    void PlaceCreate_객체로부터_Place_객체를_생성한다() {
        // given
        PlaceCreate create = PlaceCreate.from(PlaceFixture.createRequest, LocationFixture.create());

        // when
        Place place = Place.create(create);

        // then
        assertAll(
                () -> assertThat(place.getId()).isNull(),
                () -> assertThat(place.getName()).isEqualTo(create.name()),
                () -> assertThat(place.getPlatformId()).isEqualTo(create.platformId()),
                () -> assertThat(place.getAddress()).isEqualTo(create.address()),
                () -> assertThat(place.getExhibitions()).isEmpty(),
                () -> assertThat(place.getLocation().getGeometry()).isEqualTo(create.location().getGeometry())
        );
    }
}