package org.recordy.server.record.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.util.BookmarkFixture;
import org.recordy.server.util.RecordFixture;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.bookmark.repository.impl.BookmarkJpaRepository;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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
    private BookmarkJpaRepository bookmarkJpaRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    private static LocalDateTime now;
    private static LocalDateTime sevenDaysAgo;
    private static LocalDateTime eightDaysAgo;

    @BeforeAll
    static void setup() {
        now = LocalDateTime.now();
        sevenDaysAgo = now.minus(7, ChronoUnit.DAYS);
        eightDaysAgo = now.minus(8, ChronoUnit.DAYS);
    }

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
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(2L, PageRequest.ofSize(1));

        //then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
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
    void findAllByUserIdOrderByCreatedAtDesc를_통해_userId를_기반으로_레코드_데이터를_조회할_수_있다() {
        // given
        // userId가 1인 레코드 : {1, 2, 5}
        long userId = 1;

        //when
        Slice<Record> result = recordRepository.findAllByUserIdOrderByIdDesc(userId, null, PageRequest.ofSize(10));

        //then
        assertAll(
                () -> assertThat(result.get()).hasSize(3),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(1L),
                () -> assertThat((result.hasNext())).isFalse()
        );

    }

    @Test
    void findAllByIdAfterOrderByIdDesc를_통해_cursor_값이_null일_경우_최신순으로_레코드_데이터를_조회할_수_있다() {
        // given
        int size = 10;

        // when
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(null, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(6),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(6L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(3).getId()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(4).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(5).getId()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void findAllByIdAfterOrderByIdDesc를_통해_커서보다_오래된_레코드_데이터를_최신순으로_조회할_수_있다() {
        // given
        long cursor = 4L;
        int size = 2;

        // when
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.hasNext()).isTrue()
        );
    }

    @Test
    void findAllByIdAfterOrderByIdDesc를_통해_커서가_가장_오래된_레코드_데이터라면_아무것도_반환하지_않는다() {
        // given
        long cursor = 1L;
        int size = 2;

        // when
        var result = recordRepository.findAllByIdAfterOrderByIdDesc(cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void findAllBySubscribingUserIdOrderByIdDesc를_통해_구독한_사용자의_레코드_데이터를_최신순으로_조회할_수_있다() {
        // given
        // user 1의 레코드 : {1, 2, 5}
        // user 2의 레코드 : {3, 4, 6}
        long userId = 1;
        int size = 3;

        // user 1 -> user 2 구독
        subscribeRepository.save(Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build()
        );

        // when
        Slice<Record> result = recordRepository.findAllBySubscribingUserIdOrderByIdDesc(userId, null, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.get()).hasSize(3),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(6L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(3L),
                () -> assertThat((result.hasNext())).isFalse()
        );
    }

    @Test
    void countAllByUserId를_통해_특정_사용자가_올린_레코드_데이터의_수를_구할_수_있다() {
        //given
        //when
        long result = recordRepository.countAllByUserId(1L);

        //then
        assertAll(
                () -> assertThat(result).isEqualTo(3)
        );
    }

    @Test
    void findMaxId를_통해_현재_모든_레코드_데이터_중_가장_큰_id값을_구할_수_있다() {
        //given
        //when
        Long maxId = recordRepository.findMaxId();

        //then
        assertThat(maxId).isEqualTo(6);
    }

    @Test
    void count를_통해_현재_모든_레코드_데이터의_개수를_구할_수_있다() {
        //given
        //when
        Long result = recordRepository.count();

        //then
        assertAll(
                () -> assertThat(result).isEqualTo(6)
        );
    }
}