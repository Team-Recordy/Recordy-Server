package org.recordy.server.exhibition.service;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.place.domain.Place;
import org.recordy.server.util.PlaceFixture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ExhibitionServiceTest extends FakeContainer {

    private static final ExhibitionCreateRequest request = new ExhibitionCreateRequest(
            "test",
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            false,
            "http://example.com",
            PlaceFixture.ID
    );

    @Test
    void Exhibition_객체를_생성해서_저장할_수_있다() {
        // when
        Place place = placeRepository.save(PlaceFixture.create());
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "test",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                false,
                "http://example.com",
                place.getId()
        );
        Exhibition result = exhibitionService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(request.name()),
                () -> assertThat(result.getStartDate()).isEqualTo(request.startDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(request.endDate()),
                () -> assertThat(result.isFree()).isEqualTo(request.isFree()),
                () -> assertThat(result.getUrl()).isEqualTo(request.url()),
                () -> assertThat(result.getPlace().getId()).isEqualTo(request.placeId())
        );
    }

    @Test
    void Exhibition_객체를_수정할_수_있다() {
        // given
        Exhibition exhibition = exhibitionService.create(request);
        ExhibitionUpdate update = new ExhibitionUpdate(
                exhibition.getId(),
                "update",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                true,
                "https://example.com"
        );

        // when
        exhibitionService.update(update);

        // then
        Exhibition result = exhibitionRepository.findById(exhibition.getId());
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(update.name()),
                () -> assertThat(result.getStartDate()).isEqualTo(update.startDate()),
                () -> assertThat(result.getEndDate()).isEqualTo(update.endDate()),
                () -> assertThat(result.isFree()).isEqualTo(update.isFree()),
                () -> assertThat(result.getUrl()).isEqualTo(update.url())
        );
    }

    @Test
    void 수정하려는_Exhibition_객체를_찾지_못하면_예외가_발생한다() {
        // given
        ExhibitionUpdate update = new ExhibitionUpdate(
                99L,
                "update",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                true,
                "https://example.com"
        );

        // when, then
        assertThatThrownBy(() -> exhibitionService.update(update))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }

    @Test
    void Exhibition_객체를_삭제할_수_있다() {
        // given
        Exhibition exhibition = exhibitionService.create(request);

        // when
        exhibitionService.delete(exhibition.getId());

        // then
        assertThatThrownBy(() -> exhibitionRepository.findById(exhibition.getId()))
                .isInstanceOf(ExhibitionException.class)
                .hasMessage(ErrorMessage.EXHIBITION_NOT_FOUND.getMessage());
    }
}