package org.recordy.server.exhibition.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExhibitionRepositoryTest extends IntegrationTest {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Test
    void 전시_객체를_저장할_수_있다() {
        // given
        long id = 1L;
        Exhibition exhibition = ExhibitionFixture.create(id);

        // when
        Exhibition result = exhibitionRepository.save(exhibition);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getCreatedAt()).isNotNull(),
                () -> assertThat(result.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 전시_객체를_저장할_때_createdAt_와_updatedAt_값이_자동으로_저장된다() {
        // given
        long id = 1L;
        Exhibition exhibition = ExhibitionFixture.create(id);

        // when
        Exhibition result = exhibitionRepository.save(exhibition);

        // then
        assertAll(
                () -> assertThat(result.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(result.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void 전시_객체를_수정할_수_있다() {
        // given
        long id = 1;
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(id));

        ExhibitionUpdate update = new ExhibitionUpdate(
                id,
                "수정된 전시",
                LocalDate.now(),
                LocalDate.now()
        );

        // when
        exhibitionRepository.save(exhibition.update(update));

        // then
        Exhibition result = exhibitionRepository.findById(id);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo(update.name()),
                () -> assertThat(result.getStartDate()).isEqualTo(update.startDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(update.endDate()),
                () -> assertThat(result.getCreatedAt()).isEqualTo(exhibition.getCreatedAt())
        );
    }

    @Test
    void 전시_객체가_수정될_때_updatedAt_값이_자동으로_저장된다() {
        // given
        long id = 1;
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(id));

        ExhibitionUpdate update = new ExhibitionUpdate(
                id,
                "수정된 전시",
                LocalDate.now(),
                LocalDate.now()
        );

        // when
        exhibitionRepository.save(exhibition.update(update));

        // then
        Exhibition result = exhibitionRepository.findById(id);
        assertThat(result.getUpdatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void 전시_객체를_삭제할_수_있다() {
        // given
        long id = 1;
        exhibitionRepository.save(ExhibitionFixture.create(id));

        // when
        exhibitionRepository.deleteById(id);

        // then
        assertThatThrownBy(() -> exhibitionRepository.findById(id))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }

    @Test
    void 존재하지_않는_전시_객체를_삭제할_경우_아무일도_일어나지_않는다() {
        // given
        long id = 1;
        exhibitionRepository.save(ExhibitionFixture.create(id));

        // when, then
        assertDoesNotThrow(() -> exhibitionRepository.deleteById(100));
    }

    @Test
    void 전시_id로부터_전시_객체를_조회할_수_있다() {
        // given
        long id = 1;
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(id));

        // when
        Exhibition result = exhibitionRepository.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(exhibition.getId()),
                () -> assertThat(result.getName()).isEqualTo(exhibition.getName()),
                () -> assertThat(result.getStartDate()).isEqualTo(exhibition.getStartDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(exhibition.getEndDate())
        );
    }

    @Test
    void 존재하지_않는_전시_객체의_id로_조회할_경우_예외가_발생한다() {
        // given
        long id = 1;
        exhibitionRepository.save(ExhibitionFixture.create(id));

        // when, then
        assertThatThrownBy(() -> exhibitionRepository.findById(100))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }
}