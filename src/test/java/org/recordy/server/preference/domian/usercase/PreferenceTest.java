package org.recordy.server.preference.domian.usercase;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.preference.domain.Preference;
import static org.assertj.core.api.Assertions.assertThat;


class PreferenceTest {

    @Test
    void from을_통해_Preference_객체를_생성할_수_있다() {
        // given
        Map<Keyword, Long> keywordAndCounts = Map.of(
                Keyword.강렬한, 50L,
                Keyword.감각적인 , 100L,
                Keyword.깔끔한, 16L,
                Keyword.덕후몰이,14L,
                Keyword.귀여운, 20L
        );

        // when
        Preference preference = Preference.from(keywordAndCounts);

        // then
        assertAll(
                () -> assertThat(preference.getKeywordAndCounts()).isEqualTo(keywordAndCounts),
                () -> assertThat(preference.getSum()).isEqualTo(200L)
        );
    }

    @Test
    void getNormalizedTopKeywords를_통해_상위_3개의_키워드를_백분율로_정렬할_수_있다() {
        // given
        Map<Keyword, Long> keywordAndCounts = Map.of(
                Keyword.강렬한, 50L,
                Keyword.감각적인 , 100L,
                Keyword.깔끔한, 16L,
                Keyword.덕후몰이,14L,
                Keyword.귀여운, 20L
        );
        Preference preference = Preference.from(keywordAndCounts);

        // when
        Map<Keyword, Long> result = preference.getNormalizedTopKeywords(3);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(Keyword.감각적인)).isEqualTo(50L),
                () -> assertThat(result.get(Keyword.강렬한)).isEqualTo(25L),
                () -> assertThat(result.get(Keyword.귀여운)).isEqualTo(10L)
        );
    }
}