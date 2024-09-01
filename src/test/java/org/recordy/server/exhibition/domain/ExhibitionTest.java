package org.recordy.server.exhibition.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;

import static org.assertj.core.api.Assertions.assertThat;

class ExhibitionTest {

    @Test
    void Exhibition_엔티티_객체로부터_Exhibition_객체를_생성한다() {
        // given
        long id = 1L;
        ExhibitionEntity entity = ExhibitionEntity.from(Exhibition.create(new ExhibitionCreate(id)));

        // when
        Exhibition exhibition = Exhibition.from(entity);

        // then
        assertThat(exhibition.getId()).isEqualTo(id);
    }
}