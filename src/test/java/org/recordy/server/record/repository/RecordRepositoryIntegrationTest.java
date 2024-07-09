package org.recordy.server.record.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.impl.UploadJpaRepository;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.record_stat.domain.Bookmark;
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

    @Test
    void save를_통해_레코드_데이터를_저장할_수_있다() {
        // given
        Record record = DomainFixture.createRecord();

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
        Record record = DomainFixture.createRecord();

        // when
        Record savedRecord = recordRepository.save(record);
        List<UploadEntity> uploads = uploadRepository.findAllByRecord(RecordEntity.from(savedRecord));

        // then
        assertAll(
                () -> assertThat(uploads).hasSize(3),
                () -> assertThat(uploads.get(0).getKeyword().toDomain()).isEqualTo(DomainFixture.KEYWORD_1),
                () -> assertThat(uploads.get(1).getKeyword().toDomain()).isEqualTo(DomainFixture.KEYWORD_2),
                () -> assertThat(uploads.get(2).getKeyword().toDomain()).isEqualTo(DomainFixture.KEYWORD_3)
        );
        assertAll(
                () -> assertThat(uploads.get(0).getRecord().getId()).isEqualTo(savedRecord.getId()),
                () -> assertThat(uploads.get(1).getRecord().getId()).isEqualTo(savedRecord.getId()),
                () -> assertThat(uploads.get(2).getRecord().getId()).isEqualTo(savedRecord.getId())
        );
    }

    @Test
    void save를_통해_저장한_업로드는_관련된_레코드를_참조할_수_있다() {
        // given
        Record record = DomainFixture.createRecord();
        Record savedRecord = recordRepository.save(record);

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
        //given
        Record record = DomainFixture.createRecord();
        Record savedRecord = recordRepository.save(record);

        //when
        recordRepository.deleteById(savedRecord.getId());
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(0, PageRequest.ofSize(1));

        //then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void findAllByUserIdOrderByCreatedAtDesc를_통해_userId를_기반으로_레코드_데이터를_조회할_수_있다() {
        //given
        long userId = 1;
        long cursor = 4L;
        int size = 2;

        //when
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(cursor, PageRequest.ofSize(size));

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
                () -> assertThat(result.getContent()).hasSize(5),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(3).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(4).getId()).isEqualTo(1L),
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
        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(new Record(
                        2L,
                        new FileUrl(VIDEO_URL, THUMBNAIL_URL),
                        LOCATION,
                        CONTENT,
                        KEYWORDS,
                        createUser(UserStatus.ACTIVE)
                ))
                .build());

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(3);

        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getId()).isEqualTo(2L)
        );

        System.out.println("result.get(1).getId() = " + result.get(1).getId());
        System.out.println("result.get(2).getId() = " + result.get(2).getId());
    }

    @Test
    void findAllOrderByPopularity를_통해_조회한_레코드는_조회수보다_저장수에서_더_큰_가중치를_얻는다() {
        // given
        viewRepository.save(new Record(
                2L,
                new FileUrl(VIDEO_URL, THUMBNAIL_URL),
                LOCATION,
                CONTENT,
                KEYWORDS,
                createUser(UserStatus.ACTIVE)
        ), createUser(UserStatus.ACTIVE));

        bookmarkRepository.save(Bookmark.builder()
                .user(createUser(UserStatus.ACTIVE))
                .record(new Record(
                        3L,
                        new FileUrl(VIDEO_URL, THUMBNAIL_URL),
                        LOCATION,
                        CONTENT,
                        KEYWORDS,
                        createUser(UserStatus.ACTIVE)
                ))
                .build()
        );

        // when
        List<Record> result = recordRepository.findAllOrderByPopularity(3);

        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L)
        );
    }
}