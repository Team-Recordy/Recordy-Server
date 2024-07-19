package org.recordy.server.preference.domian.usercase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.preference.domain.usecase.Preference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class PreferenceTest {

    @Test
    void of를_통해_취향_정보를_가지고_상위_3개의_취향을_내림차순으로_정렬한_Preference형태로_리턴할_수_있다() {
        //given
        Long userId = 1L;
        Map<Keyword, Long> preference = Map.of(
                Keyword.강렬한, 50L,
                Keyword.감각적인 , 100L,
                Keyword.깔끔한, 16L,
                Keyword.덕후몰이,14L,
                Keyword.귀여운, 20L
        );

        //when
        Preference result = Preference.of(preference);

        //then
        assertAll(
                () -> assertThat(result.preference().size()).isEqualTo(3),
                () -> assertThat(result.preference().get(0).get(0)).isEqualTo("감각적인"),
                () -> assertThat(result.preference().get(1).get(0)).isEqualTo("강렬한"),
                () -> assertThat(result.preference().get(2).get(0)).isEqualTo("귀여운")

        );
    }

    @Test
    void of를_통해_얻은_Preference는_키워드에_대한_값을_백분율로_리턴한다() {
        //given
        Long userId = 1L;
        Map<Keyword, Long> preference = Map.of(
                Keyword.강렬한, 50L,
                Keyword.감각적인 , 100L,
                Keyword.귀여운, 20L,
                Keyword.깔끔한, 16L,
                Keyword.덕후몰이,14L
        );

        //when
        Preference result = Preference.of(preference);

        //then
        assertAll(
                () -> assertThat(result.preference().size()).isEqualTo(3),
                () -> assertThat(result.preference().get(0).get(1)).isEqualTo("50"),
                () -> assertThat(result.preference().get(1).get(1)).isEqualTo("25"),
                () -> assertThat(result.preference().get(2).get(1)).isEqualTo("10")

        );

    }

}