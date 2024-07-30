package org.recordy.server.bookmark.service;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.service.BookmarkService;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestExecutionListeners;

public class BookmarkServiceTest {

    private RecordService recordService;
    private BookmarkService bookmarkService;
    private BookmarkRepository bookmarkRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordService = fakeContainer.recordService;
        bookmarkService = fakeContainer.bookmarkService;
        bookmarkRepository = fakeContainer.bookmarkRepository;
        UserRepository userRepository = fakeContainer.userRepository;
        RecordRepository recordRepository = fakeContainer.recordRepository;

        userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));
        recordRepository.save(DomainFixture.createRecord(1));
        recordRepository.save(DomainFixture.createRecord(2));
        recordRepository.save(DomainFixture.createRecord(3));
    }

    @Test
    void bookmark을_통해_북마크를_생성하고_true를_반환받을_수_있다() {
        // given, when
        boolean result = bookmarkService.bookmark(DomainFixture.USER_ID, DomainFixture.RECORD_ID);

        // then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> assertThat(bookmarkRepository.existsByUserIdAndRecordId(DomainFixture.USER_ID, DomainFixture.RECORD_ID)).isTrue()
        );
    }

    @Test
    void bookmark을_통해_북마크를_해제하고_false를_반환받을_수_있다() {
        // given
        bookmarkService.bookmark(DomainFixture.USER_ID, DomainFixture.RECORD_ID);

        // when
        boolean result = bookmarkService.bookmark(DomainFixture.USER_ID, DomainFixture.RECORD_ID);

        // then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> assertThat(bookmarkRepository.existsByUserIdAndRecordId(DomainFixture.USER_ID, DomainFixture.RECORD_ID)).isFalse()
        );
    }

        @Test
    void findBookmarks를_통해_주어진_레코드_리스트에_대한_북마크_여부예_대한_리스트를_반환받을_수_있다() {
        //given
        Long userId = 1L;
        bookmarkService.bookmark(userId, 1);
        bookmarkService.bookmark(userId, 3);
        List<Record> records = List.of(
                DomainFixture.createRecord(1),
                DomainFixture.createRecord(2),
                DomainFixture.createRecord(3)
        );

        //when
        List<Boolean> result = bookmarkService.findBookmarks(userId, records);

        //then
        assertAll(
                () -> result.get(0).equals(true),
                () -> result.get(1).equals(false),
                () -> result.get(2).equals(true)
        );

    }
}
