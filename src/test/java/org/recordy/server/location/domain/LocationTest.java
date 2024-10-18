package org.recordy.server.location.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.LocationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LocationTest {

    @Test
    void Location_엔티티_객체로부터_Location_객체를_생성한다() {
        // given
        long id = 1L;
        LocationEntity entity = LocationEntity.from(LocationFixture.create(id));

        // when
        Location location = Location.from(entity);

        // then
        assertAll(
                () -> assertThat(location.getId()).isEqualTo(id),
                () -> assertThat(location.getGeometry()).isEqualTo(entity.getGeometry()),
                () -> assertThat(location.getCreatedAt()).isEqualTo(entity.getCreatedAt()),
                () -> assertThat(location.getUpdatedAt()).isEqualTo(entity.getUpdatedAt())
        );
    }
}