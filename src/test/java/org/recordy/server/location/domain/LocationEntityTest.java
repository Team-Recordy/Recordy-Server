package org.recordy.server.location.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.LocationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LocationEntityTest {

    @Test
    void Location_객체로부터_LocationEntity_객체를_생성한다() {
        // given
        long id = 1L;
        Location location = LocationFixture.create(id);

        // when
        LocationEntity entity = LocationEntity.from(location);

        // then
        assertAll(
                () -> assertThat(entity.getId()).isEqualTo(id),
                () -> assertThat(entity.getGeometry()).isEqualTo(location.getGeometry()),
                () -> assertThat(entity.getCreatedAt()).isEqualTo(location.getCreatedAt()),
                () -> assertThat(entity.getUpdatedAt()).isEqualTo(location.getUpdatedAt())
        );
    }
}