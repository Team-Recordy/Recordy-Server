package org.recordy.server.record.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.UploadEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/record-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
class UploadRepositoryIntegrationTest {

    @Autowired
    private UploadRepository uploadRepository;

    @Test
    void findAllByRecordId를_통해_해당_레코드의_모든_업로드를_조회할_수_있다() {
        // given
        long recordId = 1L;

        // when
        List<UploadEntity> result = uploadRepository.findAllByRecordId(recordId);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getId()).isEqualTo(6L)
        );
    }

    @Test
    void countAllByUserIdGroupByKeyword를_통해_해당_유저의_모든_업로드를_키워드별로_그룹화하여_조회할_수_있다() {
        // given
        long userId = 1L;

        // when
        Map<Keyword, Long> result = uploadRepository.countAllByUserIdGroupByKeyword(userId);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(Keyword.감각적인)).isEqualTo(2L),
                () -> assertThat(result.get(Keyword.강렬한)).isEqualTo(2L)
        );
    }
}