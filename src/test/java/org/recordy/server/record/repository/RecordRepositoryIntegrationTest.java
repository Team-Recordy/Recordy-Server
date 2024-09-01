package org.recordy.server.record.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.domain.ViewEntity;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.view.repository.ViewRepository;
import org.recordy.server.bookmark.repository.impl.BookmarkJpaRepository;
import org.recordy.server.view.repository.impl.ViewJpaRepository;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.util.DomainFixture.*;

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
    private UploadRepository uploadRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private BookmarkJpaRepository bookmarkJpaRepository;

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ViewJpaRepository viewJpaRepository;

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
        Record record = DomainFixture.createRecord(6);

        // when
        Record result = recordRepository.save(record);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getFileUrl().videoUrl()).isEqualTo(DomainFixture.VIDEO_URL),
                () -> assertThat(result.getFileUrl().thumbnailUrl()).isEqualTo(DomainFixture.THUMBNAIL_URL),
                () -> assertThat(result.getLocation()).isEqualTo(DomainFixture.LOCATION),
                () -> assertThat(result.getContent()).isEqualTo(CONTENT)
        );
    }

    @Test
    void save를_통해_저장한_업로드는_관련된_레코드를_참조할_수_있다() {
        // given
        Record savedRecord = recordRepository.save(DomainFixture.createRecord(6));

        // when
        List<UploadEntity> uploads = uploadRepository.findAllByRecordId(savedRecord.getId());

        // then
        assertAll(
                () -> assertThat(uploads).hasSize(3),
                () -> assertThat(uploads.get(0).getRecord().getId()).isEqualTo(savedRecord.getId()),
                () -> assertThat(uploads.get(1).getRecord().getId()).isEqualTo(savedRecord.getId()),
                () -> assertThat(uploads.get(2).getRecord().getId()).isEqualTo(savedRecord.getId())
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
        // userId 순서 : {1, 1, 2, 2, 1}
        long userId = 1;
        long cursor = 4L;
        int size = 2;

        //when
        Slice<Record> result = recordRepository.findAllByUserIdOrderByIdDesc(userId, cursor, PageRequest.ofSize(size));

        //then
        assertAll(
                () -> assertThat(result.get()).hasSize(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(1L),
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
    void findAllOrderByPopularity를_통해_인기순으로_레코드_데이터를_조회할_수_있다() {
        // given
        viewRepository.save(View.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(1L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(sevenDaysAgo)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(2L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .createdAt(sevenDaysAgo)
                        .build())
                .build());

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(PageRequest.of(0, 2))
                .getContent();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(2L)
        );
    }

    @Test
    void findAllOrderByPopularity를_통해_계산한_인기순은_7일간의_데이터만_반영한다() {
        // given
        // 8일 전에 1L,2L을 저장, 7일 전에 3L를 저장
        // 8일 전에 4L,5L을 시청, 7일 전에 6L를 시청
        saveBookmarkWithCreatedAt(1, eightDaysAgo);
        saveBookmarkWithCreatedAt(2, eightDaysAgo);
        saveBookmarkWithCreatedAt(3, sevenDaysAgo);
        saveViewWithCreatedAt(4, eightDaysAgo);
        saveViewWithCreatedAt(5, eightDaysAgo);
        saveViewWithCreatedAt(6, sevenDaysAgo);

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(PageRequest.of(0, 2))
                .getContent();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.get(1).getId()).isEqualTo(6L)
        );
    }

    private void saveBookmarkWithCreatedAt(long recordId, LocalDateTime createdAt) {
        Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(createRecord(recordId))
                .build());
        BookmarkEntity bookmarkEntity = bookmarkJpaRepository.findById(bookmark.getId())
                .orElseThrow();
        bookmarkEntity.setCreatedAt(createdAt.plusMinutes(1));
    }

    private void saveViewWithCreatedAt(long recordId, LocalDateTime createdAt) {
        View view = viewRepository.save(View.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(createRecord(recordId))
                .build());
        ViewEntity viewEntity = viewJpaRepository.findById(view.getId())
                .orElseThrow();
        viewEntity.setCreatedAt(createdAt.plusMinutes(1));
    }

    @Test
    void findAllOrderByPopularity를_통해_조회한_레코드는_조회수보다_저장수에서_더_큰_가중치를_얻는다() {
        // given
        viewRepository.save(View.builder()
                .record(createRecord(1))
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );
        viewRepository.save(View.builder()
                .record(createRecord(1))
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );
        viewRepository.save(View.builder()
                .record(createRecord(1))
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );
        viewRepository.save(View.builder()
                .record(createRecord(2))
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );
        // 1번 레코드 3번 시청, 2번 레코드 1번 시청

        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(createRecord(3))
                .createdAt(sevenDaysAgo)
                .build()
        );
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(createRecord(4))
                .createdAt(sevenDaysAgo)
                .build()
        );
        // 3번 레코드 1번 저장, 4번 레코드 1번 저장

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(PageRequest.of(0, 4))
                .getContent();

        // then
        assertAll(
                () -> assertThat(result).hasSize(4),
                () -> assertThat(result.get(0).getId()).isEqualTo(1),
                () -> assertThat(result.get(1).getId()).isIn(3L, 4L),
                () -> assertThat(result.get(2).getId()).isIn(3L, 4L),
                () -> assertThat(result.get(3).getId()).isEqualTo(2)
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