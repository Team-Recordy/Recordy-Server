package org.recordy.server.record_stat.service;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.Slice;

public class RecordStatServiceTest {
    private RecordStatService recordStatService;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordStatService = fakeContainer.recordStatService;
        UserRepository userRepository = fakeContainer.userRepository;
        RecordRepository recordRepository = fakeContainer.recordRepository;

        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));
        recordRepository.save(DomainFixture.createRecord());
        recordRepository.save(DomainFixture.createRecord());
        recordRepository.save(DomainFixture.createRecord());
    }

    @Test
    void bookmark을_통해_북마크를_생성할_수_있다() {
        //given
        //when
        Bookmark bookmark = recordStatService.bookmark(DomainFixture.USER_ID, DomainFixture.RECORD_ID);

        // then
        assertAll(
                () -> assertThat(bookmark).isNotNull(),
                () -> assertThat(bookmark.getUser().getId()).isEqualTo(DomainFixture.USER_ID),
                () -> assertThat(bookmark.getRecord().getId()).isEqualTo(DomainFixture.RECORD_ID)
        );
    }

    @Test
    void deleteBookmark를_통해_북마크를_삭제할_수_있다() {
        // given
        recordStatService.bookmark(1, 1);

        // when
        recordStatService.deleteBookmark(1,1);

        // then
        Slice<Record> result = recordStatService.getBookmarkedRecords(1, 7, 10);

        assertAll(
                () -> assertThat(result.getContent()).hasSize(0)
        );
    }

    @Test
    void getBookmarkedRecords를_통해_커서_이후의_북마크된_레코드를_최신_순서로_읽을_수_있다() {
        // given
        recordStatService.bookmark(1, 1);
        recordStatService.bookmark(2, 1);
        recordStatService.bookmark(1, 2);
        recordStatService.bookmark(2, 2);
        recordStatService.bookmark(1, 3);
        recordStatService.bookmark(2, 3);

        // when
        Slice<Record> result = recordStatService.getBookmarkedRecords(1, 7, 10);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(3),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getBookmarkedRecords를_통해_커서가_제일_오래된_값이라면_아무것도_반환되지_않는다() {
        // given
        recordStatService.bookmark(1, 1);
        recordStatService.bookmark(2, 1);
        recordStatService.bookmark(1, 2);
        recordStatService.bookmark(2, 2);
        recordStatService.bookmark(1, 3);
        recordStatService.bookmark(2, 3);

        // when
        Slice<Record> result = recordStatService.getBookmarkedRecords(1, 1, 10);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }
}
