package org.recordy.server.exhibition.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.PlaceFixture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ExhibitionTest {

    @Test
    void Exhibition_엔티티_객체로부터_Exhibition_객체를_생성한다() {
        // given
        long id = 1L;
        ExhibitionEntity entity = ExhibitionEntity.from(ExhibitionFixture.create(id));

        // when
        Exhibition exhibition = Exhibition.from(entity);

        // then
        assertAll(
                () -> assertThat(exhibition.getId()).isEqualTo(entity.getId()),
                () -> assertThat(exhibition.getName()).isEqualTo(entity.getName()),
                () -> assertThat(exhibition.getStartDate()).isEqualTo(entity.getStartDate()),
                () -> assertThat(exhibition.getEndDate()).isEqualTo(entity.getEndDate()),
                () -> assertThat(exhibition.isFree()).isEqualTo(entity.isFree()),
                () -> assertThat(exhibition.getPlace().getId()).isEqualTo(entity.getPlace().getId())
        );
    }

    @Test
    void ExhibitionCreate_객체로부터_Exhibition_객체를_생성한다() {
        // given
        ExhibitionCreate create = new ExhibitionCreate(
                1L,
                "name",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                false,
                PlaceFixture.create()
        );

        // when
        Exhibition exhibition = Exhibition.create(create);

        // then
        assertAll(
                () -> assertThat(exhibition.getId()).isEqualTo(create.id()),
                () -> assertThat(exhibition.getName()).isEqualTo(create.name()),
                () -> assertThat(exhibition.getStartDate()).isEqualTo(create.startDate()),
                () -> assertThat(exhibition.getEndDate()).isEqualTo(create.endDate()),
                () -> assertThat(exhibition.isFree()).isEqualTo(create.isFree()),
                () -> assertThat(exhibition.getPlace()).isEqualTo(create.place())
        );
    }

    @Test
    void ExhibitionUpdate_객체로부터_Exhibition_객체를_수정한다() {
        // given
        Exhibition exhibition = ExhibitionFixture.create(1L);
        ExhibitionUpdate update = new ExhibitionUpdate(
                exhibition.getId(),
                "updated name",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                true
        );

        // when
        Exhibition updatedExhibition = exhibition.update(update);

        // then
        assertAll(
                () -> assertThat(updatedExhibition.getId()).isEqualTo(exhibition.getId()),
                () -> assertThat(updatedExhibition.getName()).isEqualTo(update.name()),
                () -> assertThat(updatedExhibition.getStartDate()).isEqualTo(update.startDate()),
                () -> assertThat(updatedExhibition.getEndDate()).isEqualTo(update.endDate()),
                () -> assertThat(updatedExhibition.isFree()).isEqualTo(update.isFree())
        );
    }

    @Test
    void ExhibitionUpdate_객체의_필드_중_null_또는_빈값이_있을_경우_기존_값을_유지한다() {
        // given
        Exhibition exhibition = ExhibitionFixture.create(1L);
        ExhibitionUpdate update = new ExhibitionUpdate(
                exhibition.getId(),
                null,
                null,
                null,
                false
        );

        // when
        Exhibition updatedExhibition = exhibition.update(update);

        // then
        assertAll(
                () -> assertThat(updatedExhibition.getId()).isEqualTo(exhibition.getId()),
                () -> assertThat(updatedExhibition.getName()).isEqualTo(exhibition.getName()),
                () -> assertThat(updatedExhibition.getStartDate()).isEqualTo(exhibition.getStartDate()),
                () -> assertThat(updatedExhibition.getEndDate()).isEqualTo(exhibition.getEndDate()),
                () -> assertThat(updatedExhibition.isFree()).isEqualTo(exhibition.isFree())
        );
    }
}