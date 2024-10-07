package org.recordy.server.record.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.BookmarkFixture;
import org.recordy.server.util.RecordFixture;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/record-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
class RecordRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save를_통해_레코드_데이터를_저장할_수_있다() {
        // given
        Record record = RecordFixture.create();

        // when
        Long id = recordRepository.save(record);

        // then
        Record result = recordRepository.findById(id);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getUploader().getId()).isEqualTo(record.getUploader().getId()),
                () -> assertThat(result.getContent()).isEqualTo(record.getContent()),
                () -> assertThat(result.getFileUrl()).isEqualTo(record.getFileUrl())
        );
    }

    @Test
    void deleteById를_통해_레코드를_삭제할_수_있다() {
        // when
        recordRepository.deleteById(1);

        // then
        assertThatThrownBy(() -> recordRepository.findById(1))
                .isInstanceOf(RecordException.class)
                .hasMessage(ErrorMessage.RECORD_NOT_FOUND.getMessage());
    }

    @Test
    void 특정한_장소와_관련한_레코드_리스트를_조회할_수_있다() {
        // given
        // placeId가 1인 레코드 : {1, 2, 3, 4, 5, 6}

        // when
        Slice<RecordGetResponse> result = recordRepository.findAllByPlaceIdOrderByIdDesc(1, 1, null, 6);

        // then
        assertAll(
                () -> assertThat(result.get()).hasSize(6),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(6L),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(2).id()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(3).id()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(4).id()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(5).id()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void 특정한_장소와_관련한_커서보다_작은_id를_가진_레코드_리스트를_조회할_수_있다() {
        // given
        // placeId가 1인 레코드 : {1, 2, 3, 4, 5, 6}

        // when
        Slice<RecordGetResponse> result = recordRepository.findAllByPlaceIdOrderByIdDesc(1, 1, 4L, 3);

        // then
        assertAll(
                () -> assertThat(result.get()).hasSize(3),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(2).id()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void 레코드_리스트_조회_시_해당_레코드가_사용자의_것인지_아닌지_판별할_수_있다() {
        // given
        // userId가 1인 레코드 : {1, 2, 5}
        // placeId가 1인 레코드 : {1, 2, 3, 4, 5, 6}

        // when
        Slice<RecordGetResponse> result = recordRepository.findAllByPlaceIdOrderByIdDesc(1, 1, null, 6);

        // then
        assertAll(
                () -> assertThat(result.getContent().get(0).isMine()).isFalse(), // id : 6
                () -> assertThat(result.getContent().get(1).isMine()).isTrue(),  // id : 5
                () -> assertThat(result.getContent().get(2).isMine()).isFalse(), // id : 4
                () -> assertThat(result.getContent().get(3).isMine()).isFalse(), // id : 3
                () -> assertThat(result.getContent().get(4).isMine()).isTrue(),  // id : 2
                () -> assertThat(result.getContent().get(5).isMine()).isTrue()   // id : 1
        );
    }

    @Test
    void 레코드_리스트_조회_시_해당_레코드를_사용자가_북마크했는지_안했는지_판별할_수_있다() {
        // given
        // placeId가 1인 레코드 : {1, 2, 3, 4, 5, 6}
        bookmarkRepository.save(BookmarkFixture.create(DomainFixture.createUser(), RecordFixture.create(1L)));
        bookmarkRepository.save(BookmarkFixture.create(DomainFixture.createUser(), RecordFixture.create(3L)));
        bookmarkRepository.save(BookmarkFixture.create(DomainFixture.createUser(), RecordFixture.create(5L)));

        // when
        Slice<RecordGetResponse> result = recordRepository.findAllByPlaceIdOrderByIdDesc(1, 1, null, 6);

        // then
        assertAll(
                () -> assertThat(result.getContent().get(0).isBookmarked()).isFalse(), // id : 6
                () -> assertThat(result.getContent().get(1).isBookmarked()).isTrue(),  // id : 5
                () -> assertThat(result.getContent().get(2).isBookmarked()).isFalse(), // id : 4
                () -> assertThat(result.getContent().get(3).isBookmarked()).isTrue(),  // id : 3
                () -> assertThat(result.getContent().get(4).isBookmarked()).isFalse(), // id : 2
                () -> assertThat(result.getContent().get(5).isBookmarked()).isTrue()   // id : 1
        );
    }

    @Test
    void findById를_통해_id에_해당하는_record_엔티티를_조회할_수_있다() {
        // when
        Record record = recordRepository.findById(6);

        //then
        assertThat(record.getId()).isEqualTo(6);
    }

    @Test
    void findById를_통해_id에_해당하는_record_엔티티를_찾을_수_없으면_예외를_일으킨다() {
        // when, then
        assertThatThrownBy(() -> recordRepository.findById(99L))
                .isInstanceOf(RecordException.class);
    }

    @Test
    void findAllByUserIdOrderByIdDesc를_통해_userId를_기반으로_레코드_데이터를_조회할_수_있다() {
        // given
        // userId가 1인 레코드 : {1, 2, 5}
        long userId = 1;

        // when
        Slice<RecordGetResponse> result = recordRepository.findAllByUserIdOrderByIdDesc(userId, userId, null, 10);

        // then
        assertAll(
                () -> assertThat(result.get()).hasSize(3),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(2).id()).isEqualTo(1L)
        );
    }

    @Test
    void 사용자가_구독중인_사용자가_업로드한_모든_영상의_id_리스트를_조회할_수_있다() {
        // given
        // userId가 2인 사용자가 업로드한 레코드 : {3, 4, 6}
        subscribeRepository.save(Subscribe.builder()
                .subscribingUser(userRepository.findById(1))
                .subscribedUser(userRepository.findById(2))
                .build());

        // when
        List<Long> result = recordRepository.findAllIdsBySubscribingUserId(1);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result).hasSameElementsAs(List.of(3L, 4L, 6L))
        );
    }

    @Test
    void id_리스트의_각_요소로부터_일치하는_레코드_객체_리스트를_조회할_수_있다() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);

        // when
        List<Long> result = recordRepository.findAllByIds(ids, 1)
                .stream()
                .map(RecordGetResponse::id)
                .toList();

        // then
        assertAll(
                () -> assertThat(result).hasSameSizeAs(ids),
                () -> assertThat(result).hasSameElementsAs(ids)
        );
    }

    @Test
    void id_리스트의_각_요소로부터_일치하는_레코드_객체_리스트를_조회할_때_해당_레코드가_자신의_것인지_알_수_있다() {
        // given
        // userId가 1인 사용자와 연관된 레코드 : {1, 2, 5}
        List<Long> ids = List.of(1L, 2L, 3L);

        // when
        List<Boolean> result = recordRepository.findAllByIds(ids, 1)
                .stream()
                .map(RecordGetResponse::isMine)
                .toList();

        // then
        assertThat(result).hasSameElementsAs(List.of(true, true, false));
    }

    @Test
    void id_리스트의_각_요소로부터_일치하는_레코드_객체_리스트를_조회할_때_해당_레코드가_북마크_된_것인지_알_수_있다() {
        // given
        bookmarkRepository.save(BookmarkFixture.create(DomainFixture.createUser(), RecordFixture.create(1L)));
        bookmarkRepository.save(BookmarkFixture.create(DomainFixture.createUser(), RecordFixture.create(2L)));
        List<Long> ids = List.of(1L, 2L, 3L);

        // when
        List<Boolean> result = recordRepository.findAllByIds(ids, 1)
                .stream()
                .map(RecordGetResponse::isBookmarked)
                .toList();

        // then
        assertThat(result).hasSameElementsAs(List.of(true, true, false));
    }
}