package org.recordy.server.exhibition.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.ExhibitionFixture;

import static org.assertj.core.api.Assertions.assertThat;

class ExhibitionEntityTest {

    @Test
    void Exhibition_객체로부터_Exhibition_엔티티_객체를_생성한다() {
        // given
        long id = 1L;
        Exhibition exhibition = ExhibitionFixture.create(id);

        // when
        ExhibitionEntity entity = ExhibitionEntity.from(exhibition);

        // then
        assertThat(entity.getId()).isEqualTo(id);
    }
}