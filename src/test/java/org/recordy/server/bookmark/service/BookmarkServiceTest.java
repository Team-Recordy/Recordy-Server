package org.recordy.server.bookmark.service;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;

public class BookmarkServiceTest extends FakeContainer {

    @BeforeEach
    void init() {
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
}
