package org.recordy.server.keyword.domain;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class KeywordTest {

    @Test
    void decode를_통해_콤마로_연결된_채_인코딩된_문자열_utf8_코드를_해독하여_키워드_리스트로_디코딩할_수_있다() {
        // given
        String code = Base64.getEncoder().encodeToString("감각적인,강렬한".getBytes());

        // when
        List<Keyword> keywords = Keyword.decode(code);

        // then
        assertAll(
                () -> assertThat(keywords.get(0)).isEqualTo(Keyword.감각적인),
                () -> assertThat(keywords.get(1)).isEqualTo(Keyword.강렬한)
        );
    }
}