package org.recordy.server.preference.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.preference.domain.usecase.Preference;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;

class PreferenceServiceTest {
    private PreferenceService preferenceService;
    private UserRepository userRepository;
    private RecordRepository recordRepository;

    @BeforeEach
    void init() {
        preferenceService = new FakeContainer().preferenceService;
        userRepository = new FakeContainer().userRepository;
        recordRepository = new FakeContainer().recordRepository;
    }

    @Test
    void getPreferenc를_통해_사용자의_취향_키워드_상위_3개의_이름과_점수를_계산할_수_있다() {
        //given
        User user = userRepository.save(DomainFixture.createUser(1));
        Record record1 = Record.builder()
                        .id(1L)
                        .uploader(user)
                        .content("내용입니다")
                        .location("위치")
                        .fileUrl(new FileUrl("videoUrl", "thumbnailUrl"))
                        .keywords(List.of(
                                Keyword.감각적인,
                                Keyword.강렬한,
                                Keyword.귀여운
                        ))
                        .build();
        Record record2 = Record.builder()
                .id(2L)
                .uploader(user)
                .content("내용입니다")
                .location("위치")
                .fileUrl(new FileUrl("videoUrl", "thumbnailUrl"))
                .keywords(List.of(
                        Keyword.감각적인,
                        Keyword.귀여운
                ))
                .build();
        Record record3 = Record.builder()
                .id(3L)
                .uploader(user)
                .content("내용입니다")
                .location("위치")
                .fileUrl(new FileUrl("videoUrl", "thumbnailUrl"))
                .keywords(List.of(
                        Keyword.감각적인
                ))
                .build();

        recordRepository.save(record1);
        recordRepository.save(record2);
        recordRepository.save(record3);

        //when
        Preference result = preferenceService.getPreference(1L);

        //then
        assertAll(
                () -> assertThat(result.preference().size()).isEqualTo(3),
                () -> assertThat(result.preference().get(0).get(0)).isEqualTo("감각적인"),
                () -> assertThat(result.preference().get(0).get(1)).isEqualTo("귀여운"),
                () -> assertThat(result.preference().get(0).get(2)).isEqualTo("강렬한"),
                () -> assertThat(result.preference().get(1).get(0)).isEqualTo("11"),
                () -> assertThat(result.preference().get(1).get(1)).isEqualTo("22"),
                () -> assertThat(result.preference().get(1).get(2)).isEqualTo("22")
        );
    }
}
