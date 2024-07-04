package org.recordy.server.record.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.record.domain.Record;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/record-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/record-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
class RecordRepositoryIntegrationTest {

    @Autowired
    private RecordRepository recordRepository;

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
                () -> assertThat(result.getContent()).isEqualTo(DomainFixture.CONTENT)
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
}