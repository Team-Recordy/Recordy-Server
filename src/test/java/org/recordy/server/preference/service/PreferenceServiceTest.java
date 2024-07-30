package org.recordy.server.preference.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.preference.domain.Preference;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.repository.ViewRepository;

class PreferenceServiceTest {

    private PreferenceService preferenceService;
    private UserRepository userRepository;
    private RecordRepository recordRepository;
    private ViewRepository viewRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        preferenceService = fakeContainer.preferenceService;
        userRepository = fakeContainer.userRepository;
        recordRepository = fakeContainer.recordRepository;
        viewRepository = fakeContainer.viewRepository;
    }

    @Test
    void getPreference를_통해_사용자의_취향_키워드_상위_3개의_이름과_점수를_계산할_수_있다() {
        // given
        User user = userRepository.save(DomainFixture.createUser(1));

        Record record1 = DomainFixture.createRecord(1, List.of(Keyword.감각적인, Keyword.강렬한, Keyword.귀여운));
        Record record2 = DomainFixture.createRecord(2, List.of(Keyword.감각적인, Keyword.귀여운));
        Record record3 = DomainFixture.createRecord(3, List.of(Keyword.감각적인));

        List.of(record1, record2, record3)
                .forEach(record -> viewRepository.save(View.builder()
                        .user(user)
                        .record(record)
                        .build()));

        //when
        Preference result = preferenceService.getPreference(1L);

        //then
        assertAll(
                () -> assertThat(result.getKeywordAndCounts().size()).isEqualTo(3),
                () -> assertThat(result.getSum()).isEqualTo(6L),
                () -> assertThat(result.getKeywordAndCounts().get(Keyword.감각적인)).isEqualTo(3L),
                () -> assertThat(result.getKeywordAndCounts().get(Keyword.강렬한)).isEqualTo(1L),
                () -> assertThat(result.getKeywordAndCounts().get(Keyword.귀여운)).isEqualTo(2L)
        );
    }
}
