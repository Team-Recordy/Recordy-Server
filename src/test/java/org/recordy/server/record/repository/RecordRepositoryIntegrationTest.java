package org.recordy.server.record.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.impl.UploadJpaRepository;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.record_stat.repository.BookmarkRepository;
import org.recordy.server.record_stat.repository.ViewRepository;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.util.DomainFixture.*;

@SqlGroup({
        @Sql(value = "/sql/record-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/record-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
class RecordRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UploadJpaRepository uploadRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private ViewRepository viewRepository;

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
    void save를_통해_레코드와_관련한_키워드로부터_업로드_데이터를_저장할_수_있다() {
        // given
        Record record = recordRepository.save(createRecord(6));

        // when
        List<UploadEntity> uploads = uploadRepository.findAllByRecord(RecordEntity.from(record));

        // then
        assertAll(
                () -> assertThat(uploads).hasSize(3),
                () -> assertThat(uploads.get(0).getKeyword().toDomain()).isEqualTo(KEYWORD_1),
                () -> assertThat(uploads.get(1).getKeyword().toDomain()).isEqualTo(KEYWORD_2),
                () -> assertThat(uploads.get(2).getKeyword().toDomain()).isEqualTo(KEYWORD_3)
        );
    }

    @Test
    void save를_통해_저장한_업로드는_관련된_레코드를_참조할_수_있다() {
        // given
        Record savedRecord = recordRepository.save(DomainFixture.createRecord(6));

        // when
        List<UploadEntity> uploads = uploadRepository.findAllByRecord(RecordEntity.from(savedRecord));

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
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(2, PageRequest.ofSize(1));

        //then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
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
    void findAllByIdAfterOrderByIdDesc를_통해_cursor_값이_0일_경우_최신순으로_레코드_데이터를_조회할_수_있다() {
        // given
        long cursor = 0L;
        int size = 10;

        // when
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(cursor, PageRequest.ofSize(size));

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
    void findAllByIdAfterAndKeywordsOrderByIdDesc를_통해_키워드로_필터링된_레코드_데이터를_최신순으로_조회할_수_있다() {
        // given
        List<Keyword> keywords = List.of(DomainFixture.KEYWORD_1, DomainFixture.KEYWORD_2);
        long cursor = 3L;
        int size = 2;

        // when
        Slice<Record> result = recordRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywords, cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(1L),
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
        List<Record> result = recordRepository.findAllOrderByPopularity(2);

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

        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(1L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(eightDaysAgo)
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
                        .build())
                .createdAt(eightDaysAgo)
                .build());
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(3L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(sevenDaysAgo)
                .build());

        viewRepository.save(View.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(4L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(eightDaysAgo)
                .build());
        viewRepository.save(View.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(5L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(eightDaysAgo)
                .build());
        viewRepository.save(View.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(6L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build())
                .createdAt(sevenDaysAgo)
                .build());

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(2);
        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.get(1).getId()).isEqualTo(6L)
        );
    }

    @Test
    void findAllOrderByPopularity를_통해_조회한_레코드는_조회수보다_저장수에서_더_큰_가중치를_얻는다() {
        // given
        // 이미 1번 레코드를 1번 시청함
        viewRepository.save(View.builder()
                        .record(Record.builder()
                                        .id(1L)
                                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                                        .location(LOCATION)
                                        .content(CONTENT)
                                        .keywords(KEYWORDS)
                                        .uploader(createUser(UserStatus.ACTIVE))
                                        .build()
                        )
                        .user(createUser(UserStatus.ACTIVE))
                        .createdAt(sevenDaysAgo)
                        .build()
                );
        viewRepository.save(View.builder()
                .record(Record.builder()
                                .id(1L)
                                .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                                .location(LOCATION)
                                .content(CONTENT)
                                .keywords(KEYWORDS)
                                .uploader(createUser(UserStatus.ACTIVE))
                                .build()
                )
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );
        viewRepository.save(View.builder()
                .record(Record.builder()
                                .id(2L)
                                .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                                .location(LOCATION)
                                .content(CONTENT)
                                .keywords(KEYWORDS)
                                .uploader(createUser(UserStatus.ACTIVE))
                                .build()
                )
                .user(createUser(UserStatus.ACTIVE))
                .createdAt(sevenDaysAgo)
                .build()
        );

        // 1번 레코드 2번 더 시청, 2번 레코드 1번 시청
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(3L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build()
                )
                .createdAt(sevenDaysAgo)
                .build()
        );
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(Record.builder()
                        .id(4L)
                        .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                        .location(LOCATION)
                        .content(CONTENT)
                        .keywords(KEYWORDS)
                        .uploader(createUser(UserStatus.ACTIVE))
                        .build()
                )
                .createdAt(sevenDaysAgo)
                .build()
        );
        // 3번 레코드 1번 저장, 4번 레코드 1번 저장

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(4);

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
    void countAllByUserIdGroupByKeyword를_통해_사용자가_시청_저장_업로드한_모든_영상과_관련된_키워드별로_카운트할_수_있다() {
        // given
        // 현재 EXOTIC 3개 업로드했고, QUITE 3개 업로드했음
        long userId = 1L;
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

        // QUITE 키워드 영상 1번 시청함
        viewRepository.save(
                View.builder()
                    .record(DomainFixture.createRecord(2))
                    .user(DomainFixture.createUser(UserStatus.ACTIVE))
                    .build()
        );

        // EXOTIC 키워드 영상 1번 저장함
        bookmarkRepository.save(
                Bookmark.builder()
                        .user(DomainFixture.createUser(UserStatus.ACTIVE))
                        .record(DomainFixture.createRecord(5))
                        .build()
        );
        // 따라서 결과적으로 {EXOTIC:4, QUITE:4}

        // when
        Map<Keyword, Long> preference = recordRepository.countAllByUserIdGroupByKeyword(userId);
        System.out.println("preference = " + preference);

        // then
        assertAll(
                () -> assertThat(preference.size()).isEqualTo(2),
                () -> assertThat(preference.get(Keyword.EXOTIC)).isEqualTo(4),
                () -> assertThat(preference.get(Keyword.QUITE)).isEqualTo(4)
        );
    }
}