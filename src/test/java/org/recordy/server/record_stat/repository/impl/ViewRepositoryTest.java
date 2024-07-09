package org.recordy.server.record_stat.repository.impl;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record_stat.repository.ViewRepository;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/view-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/view-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ViewRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private ViewRepository viewRepository;

    @Test
    void countAllByUserIdGroupByKeyword를_통해_유저별_키워드_조회수를_조회할_수_있다() {
        // given
        long userId = 1L;

        // when
        Map<Keyword, Long> result = viewRepository.countAllByUserIdGroupByKeyword(userId);

        // then
        assertAll(
                () -> assertThat(result.get(Keyword.EXOTIC)).isEqualTo(1),
                () -> assertThat(result.get(Keyword.QUITE)).isEqualTo(1)
        );
    }
}