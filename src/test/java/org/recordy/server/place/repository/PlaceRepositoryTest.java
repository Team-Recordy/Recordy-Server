package org.recordy.server.place.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class PlaceRepositoryTest extends IntegrationTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

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
                () -> assertThat(result.getPlatformId()).isEqualTo(place.getPlatformId()),
                () -> assertThat(result.getAddress()).isEqualTo(place.getAddress())
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
        exhibitionRepository.save(ExhibitionFixture.create(place));

        // when
        PlaceGetResponse result = placeRepository.findDetailById(place.getId());

        // then
        assertThat(result.getId()).isEqualTo(place.getId());
    }

    @Test
    void 장소_객체를_id로_조회할_경우_관련된_전시_개수까지_조회할_수_있다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());

        int exhibitionSize = 10;
        for (int i = 0; i < exhibitionSize; i++) {
            exhibitionRepository.save(ExhibitionFixture.create(place));
        }

        // when
        PlaceGetResponse result = placeRepository.findDetailById(place.getId());

        // then
        assertThat(result.getExhibitionSize()).isEqualTo(10);
    }

    @Test
    void 장소_객체를_id로_조회할_경우_관련된_레코드_개수까지_조회할_수_있다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());
        User user = userRepository.save(DomainFixture.createUser());

        int recordSize = 10;
        for (int i = 0; i < recordSize; i++) {
            recordRepository.save(Record.create(RecordCreate.of(null, "", user, place)));
        }

        // when
        PlaceGetResponse result = placeRepository.findDetailById(place.getId());

        // then
        assertThat(result.getRecordSize()).isEqualTo(recordSize);
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
    void 장소_객체를_이름으로_조회할_수_있다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());
        exhibitionRepository.save(ExhibitionFixture.create(place));

        // when
        Place result = placeRepository.findByName(place.getName());

        // then
        assertThat(result.getId()).isEqualTo(place.getId());
    }

    @Test
    void 존재하지_않는_이름으로_장소_객체를_조회할_경우_예외가_발생한다() {
        // given
        Place place = placeRepository.save(PlaceFixture.create());

        // when, then
        assertThatThrownBy(() -> placeRepository.findByName("존재하지 않는 이름"))
                .isInstanceOf(PlaceException.class)
                .hasMessage(ErrorMessage.PLACE_NOT_FOUND.getMessage());
    }

    @Test
    void 전시_중_하나라도_마감일이_오늘이거나_그_이후인_장소_객체_리스트_및_전시를_조회할_수_있다() {
        // given
        Place placeIncluded = placeRepository.save(PlaceFixture.create(LocationFixture.create()));
        Place placeExcluded = placeRepository.save(PlaceFixture.create(LocationFixture.create()));

        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), placeIncluded));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), placeIncluded));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), placeExcluded));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllOrderByExhibitionStartDateDesc(PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(1),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(placeIncluded.getId()),
                () -> assertThat(result.getContent().get(0).getExhibitionSize()).isEqualTo(1)
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
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(place2.getId()),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(place1.getId()),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(place3.getId())
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
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(placeIncluded.getId())
        );
    }
}