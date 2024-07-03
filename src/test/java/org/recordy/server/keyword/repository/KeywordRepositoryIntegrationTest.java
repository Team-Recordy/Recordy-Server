package org.recordy.server.keyword.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/keyword-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/keyword-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest
class KeywordRepositoryIntegrationTest {

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    void findAll를_통해_키워드_데이터를_조회할_수_있다() {
        // when
        List<Keyword> result = keywordRepository.findAll();

        // then
        assertAll(
                () -> assertThat(result.get(0).name()).isEqualTo(DomainFixture.KEYWORD_1),
                () -> assertThat(result.get(1).name()).isEqualTo(DomainFixture.KEYWORD_2),
                () -> assertThat(result.get(2).name()).isEqualTo(DomainFixture.KEYWORD_3)
        );
    }
}