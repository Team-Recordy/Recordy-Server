package org.recordy.server.place.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class PlaceRepositoryTest extends IntegrationTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Test
    void 장소_객체를_저장할_수_있다() {
        // given
        Place place = PlaceFixture.create();

        // when
        Place result = placeRepository.save(place);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(place.getName()),
                () -> assertThat(result.getLocation().getAddress().getFormatted()).isEqualTo(place.getLocation().getAddress().getFormatted())
        );
    }

    @Test
    void 장소_객체를_저장할_때_자동으로_createdAt과_updatedAt이_채워진다() {
        // given
        Place place = PlaceFixture.create();

        // when
        Place result = placeRepository.save(place);

        // then
        assertAll(
                () -> assertThat(result.getCreatedAt()).isBefore(LocalDateTime.now(Clock.systemDefaultZone())),
                () -> assertThat(result.getUpdatedAt()).isBefore(LocalDateTime.now(Clock.systemDefaultZone()))
        );
    }

    @Test
    void 장소_객체를_id로_조회할_수_있다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());
        Exhibition exhibition = exhibitionRepository.save(ExhibitionFixture.create(place));

        // when
        Place result = placeRepository.findById(exhibition.getId());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(place.getId()),
                () -> assertThat(result.getName()).isEqualTo(place.getName()),
                () -> assertThat(result.getLocation().getId()).isEqualTo(place.getLocation().getId())
        );
    }

    @Test
    void 장소_객체를_id로_조회할_때_관련된_전시들과_장소_객체도_조회할_수_있다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create(LocationFixture.create()));

        Exhibition exhibition1 = exhibitionRepository.save(ExhibitionFixture.create("a", place));
        Exhibition exhibition2 = exhibitionRepository.save(ExhibitionFixture.create("b", place));

        // when
        Place result = placeRepository.findById(place.getId());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(place.getId()),
                () -> assertThat(result.getName()).isEqualTo(place.getName()),
                () -> assertThat(result.getLocation().getId()).isEqualTo(place.getLocation().getId()),
                () -> assertThat(result.getExhibitions().size()).isEqualTo(2),
                () -> assertThat(result.getExhibitions().get(0).getId()).isEqualTo(exhibition1.getId()),
                () -> assertThat(result.getExhibitions().get(1).getId()).isEqualTo(exhibition2.getId())
        );
    }

    @Test
    void 존재하지_않는_id로_장소_객체를_조회할_경우_예외가_발생한다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());

        // when, then
        assertThatThrownBy(() -> placeRepository.findById(place.getId() + 1))
                .isInstanceOf(PlaceException.class)
                .hasMessage(ErrorMessage.PLACE_NOT_FOUND.getMessage());
    }

    @Test
    void 전시_중_하나라도_마감일이_오늘이거나_그_이후인_장소_객체_리스트_및_전시를_조회할_수_있다() {
        // given
        Place placeIncluded = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place placeExcluded = placeRepository.save(PlaceFixture.create(LocationFixture.create()));

        List<Exhibition> exhibitions = List.of(
                exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), placeIncluded)),
                exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), placeIncluded)),
                exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), placeExcluded))
        );

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllOrderByExhibitionStartDateDesc(PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(1),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(placeIncluded.getId()),
                () -> assertThat(result.getContent().get(0).exhibitions().size()).isEqualTo(1),
                () -> assertThat(result.getContent().get(0).exhibitions().get(0).id()).isEqualTo(exhibitions.get(0).getId())
        );
    }

    @Test
    void 전시_시작일의_역순으로_장소_객체_리스트를_조회할_수_있다() {
        // given
        LocalDate firstStartDate = LocalDate.now().minusDays(10);
        LocalDate secondStartDate = LocalDate.now().minusDays(5);
        LocalDate thirdStartDate = LocalDate.now();

        Place place1 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place place2 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place place3 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));

        exhibitionRepository.save(ExhibitionFixture.create(secondStartDate, LocalDate.now(), place1));
        exhibitionRepository.save(ExhibitionFixture.create(thirdStartDate, LocalDate.now(), place2));
        exhibitionRepository.save(ExhibitionFixture.create(firstStartDate, LocalDate.now(), place3));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllOrderByExhibitionStartDateDesc(PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(3),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(place2.getId()),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(place1.getId()),
                () -> assertThat(result.getContent().get(2).id()).isEqualTo(place3.getId())
        );
    }

    @Test
    void 전시_시작일이_현재_날짜보다_뒤인_경우_조회되지_않는다() {
        // given
        Place placeIncluded = placeRepository.save(PlaceFixture.create());
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), placeIncluded));

        Place placeExcluded = placeRepository.save(PlaceFixture.create());
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), placeExcluded));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllOrderByExhibitionStartDateDesc(PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(1),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(placeIncluded.getId())
        );
    }

    @Test
    void 진행중인_공짜_전시를_가진_장소_리스트를_전시_시작일의_역순으로_조회할_수_있다() {
        // given
        Place place1 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place place2 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place place3 = placeRepository.save(PlaceFixture.create(LocationFixture.create()));

        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(3), LocalDate.now(), true, place1));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(1), LocalDate.now(), true, place2));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(2), LocalDate.now(), false, place3));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllFreeOrderByExhibitionStartDateDesc(PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(2),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(place2.getId()),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(place1.getId())
        );
    }

    @Test
    void 특정_이름을_포함하는_장소_리스트를_전시_시작일의_역순으로_조회할_수_있다() {
        // given
        Place place1 = placeRepository.save(PlaceFixture.create("국립현대미술관", LocationFixture.create()));
        Place place2 = placeRepository.save(PlaceFixture.create("이중섭미술관", LocationFixture.create()));
        Place place3 = placeRepository.save(PlaceFixture.create("박물!", LocationFixture.create()));

        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), place1));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), place2));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), place3));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllByNameOrderByExhibitionStartDateDesc(PageRequest.ofSize(10), "국립현대");

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(1),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(place1.getId())
        );
    }
}