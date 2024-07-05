package org.recordy.server.keyword.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class KeywordServiceTest {

    private KeywordService keywordService;

    @BeforeEach
    void init() {
        keywordService = new FakeContainer().keywordService;
    }

    @Test
    void getAll을_통해_현존하는_모든_키워드_데이터_리스트를_조회할_수_있다() {
        // when
        List<Keyword> result = keywordService.getAll();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(Keyword.values().length),
                () -> assertThat(result.get(0)).isEqualTo(DomainFixture.KEYWORD_1),
                () -> assertThat(result.get(1)).isEqualTo(DomainFixture.KEYWORD_2),
                () -> assertThat(result.get(2)).isEqualTo(DomainFixture.KEYWORD_3)
        );
    }
}