package org.recordy.server.bookmark.domain;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.view.domain.ViewEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookmarkEntityTest {

    @Test
    void from을_통해_Bookmark_객체로부터_BookmarkEntity_객체를_생성할_수_있다() {
        // given
        Bookmark bookmark = Bookmark.builder()
                .id(1L)
                .user(DomainFixture.createUser(UserStatus.ACTIVE))
                .record(DomainFixture.createRecord())
                .build();

        // when
        BookmarkEntity bookmarkEntity = BookmarkEntity.from(bookmark);

        // then
        assertAll(
                () -> assertThat(bookmarkEntity.getId()).isEqualTo(bookmark.getId()),
                () -> assertThat(bookmarkEntity.getRecord().getId()).isEqualTo(bookmark.getRecord().getId()),
                () -> assertThat(bookmarkEntity.getUser().getId()).isEqualTo(bookmark.getUser().getId())
        );
    }

    @Test
    void toDomain을_통해_BookmarkEntity_객체로부터_Bookmark_객체를_생성할_수_있다() {
        // given
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .id(1L)
                .record(DomainFixture.createRecordEntity())
                .user(DomainFixture.createUserEntity())
                .build();

        // when
        Bookmark bookmark = bookmarkEntity.toDomain();

        // then
        assertAll(
                () -> assertThat(bookmark.getId()).isEqualTo(bookmarkEntity.getId()),
                () -> assertThat(bookmark.getRecord().getId()).isEqualTo(bookmarkEntity.getRecord().getId()),
                () -> assertThat(bookmark.getUser().getId()).isEqualTo(bookmarkEntity.getUser().getId())
        );
    }

    @Test
    void setCreatedAt를_통해_BookmarkEntity_객체의_createAt를_초기화할_수_있다() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2002, 7, 4, 17, 15);
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .id(1L)
                .record(DomainFixture.createRecordEntity())
                .user(DomainFixture.createUserEntity())
                .build();

        //when
        bookmarkEntity.setCreatedAt(localDateTime);

        //then
        bookmarkEntity.getCreatedAt().equals(localDateTime);
    }
}