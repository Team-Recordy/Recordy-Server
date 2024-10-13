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
                () -> assertThat(place.getName()).isEqualTo(PlaceFixture.NAME),
                () -> assertThat(place.getLocation().getAddress()).isEqualTo(LocationFixture.ADDRESS),
                () -> assertThat(place.getLocation().getGooglePlaceId()).isEqualTo(LocationFixture.GOOGLE_PLACE_ID),
                () -> assertThat(place.getLocation().getGeometry()).isEqualTo(LocationFixture.POINT)
        );
    }

    @Test
    void PlaceCreate_객체로부터_Place_객체를_생성한다() {
        // given
        Location location = LocationFixture.create();
        PlaceCreate create = new PlaceCreate(PlaceFixture.NAME, location);

        // when
        Place place = Place.create(create);

        // then
        assertAll(
                () -> assertThat(place.getId()).isNull(),
                () -> assertThat(place.getName()).isEqualTo(PlaceFixture.NAME),
                () -> assertThat(place.getExhibitions()).isEmpty(),
                () -> assertThat(place.getLocation().getAddress()).isEqualTo(location.getAddress()),
                () -> assertThat(place.getLocation().getGooglePlaceId()).isEqualTo(location.getGooglePlaceId()),
                () -> assertThat(place.getLocation().getGeometry()).isEqualTo(location.getGeometry())
        );
    }
}