package org.recordy.server.exhibition.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
class ExhibitionRepositoryTest extends IntegrationTest {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private PlaceRepository placeRepository;

    private Place place;

    @BeforeEach
    void setUp() {
        place = placeRepository.save(PlaceFixture.create());
    }

    @Test
    void 전시_객체를_저장할_수_있다() {
        // given
        Exhibition exhibition = ExhibitionFixture.create(place);

        // when
        Exhibition result = exhibitionRepository.save(exhibition);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(exhibition.getName()),
                () -> assertThat(result.getStartDate()).isEqualTo(exhibition.getStartDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(exhibition.getEndDate()),
                () -> assertThat(result.getCreatedAt()).isNotNull(),
                () -> assertThat(result.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 전시_객체를_저장할_때_createdAt_와_updatedAt_값이_자동으로_저장된다() {
        // given
        Exhibition exhibition = ExhibitionFixture.create(place);

        // when
        Exhibition result = exhibitionRepository.save(exhibition);

        // then
        assertAll(
                () -> assertThat(result.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(result.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void 전시_객체를_저장할_때_가지고_있는_장소_객체가_없어도_저장된다() {
        // given
        Exhibition exhibition = ExhibitionFixture.create("nullExhibition", null);

        // when
        Exhibition result = exhibitionRepository.save(exhibition);

        // then
        assertThat(result.getPlace()).isNull();
    }

    @Test
    void 전시_객체를_수정할_수_있다() {
        // given
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(place));

        ExhibitionUpdate update = new ExhibitionUpdate(
                exhibition.getId(),
                "수정된 전시",
                LocalDate.now(),
                LocalDate.now(),
                true
        );

        // when
        exhibitionRepository.save(exhibition.update(update));

        // then
        Exhibition result = exhibitionRepository.findById(exhibition.getId());
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(exhibition.getId()),
                () -> assertThat(result.getName()).isEqualTo(update.name()),
                () -> assertThat(result.getStartDate()).isEqualTo(update.startDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(update.endDate()),
                () -> assertThat(result.getCreatedAt()).isEqualTo(exhibition.getCreatedAt()),
                () -> assertThat(result.isFree()).isEqualTo(update.isFree())
        );
    }

    @Test
    void 전시_객체가_수정될_때_updatedAt_값이_자동으로_저장된다() {
        // given
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(place));

        ExhibitionUpdate update = new ExhibitionUpdate(
                exhibition.getId(),
                "수정된 전시",
                LocalDate.now(),
                LocalDate.now(),
                true
        );

        // when
        exhibitionRepository.save(exhibition.update(update));

        // then
        Exhibition result = exhibitionRepository.findById(exhibition.getId());
        assertThat(result.getUpdatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void 전시_객체를_삭제할_수_있다() {
        // given
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(place));

        // when
        exhibitionRepository.deleteById(exhibition.getId());

        // then
        assertThatThrownBy(() -> exhibitionRepository.findById(exhibition.getId()))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }

    @Test
    void 존재하지_않는_전시_객체를_삭제할_경우_아무일도_일어나지_않는다() {
        // given
        exhibitionRepository.save(ExhibitionFixture.create(place));

        // when, then
        assertDoesNotThrow(() -> exhibitionRepository.deleteById(100));
    }

    @Test
    void 전시_id로부터_전시_객체를_조회할_수_있다() {
        // given
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(place));

        // when
        Exhibition result = exhibitionRepository.findById(exhibition.getId());

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
        exhibitionRepository.save(ExhibitionFixture.create(place));

        // when, then
        assertThatThrownBy(() -> exhibitionRepository.findById(100))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }

    @Test
    void 쿼리를_포함하는_이름을_가진_전시_객체를_최신순_정렬_리스트로_조회할_수_있다() {
        // given
        String name = "전시";
        exhibitionRepository.save(ExhibitionFixture.create(name, place));
        exhibitionRepository.save(ExhibitionFixture.create(name, place));
        exhibitionRepository.save(ExhibitionFixture.create("I Like Watching You Go", place));

        // when
        Slice<Exhibition> result = exhibitionRepository.findAllContainingName(name, null, 10);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(2),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(1)
        );
    }
}