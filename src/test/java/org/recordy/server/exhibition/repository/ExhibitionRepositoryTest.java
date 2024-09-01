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
}