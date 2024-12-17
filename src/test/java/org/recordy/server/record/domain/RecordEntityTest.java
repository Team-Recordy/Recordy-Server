package org.recordy.server.record.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.RecordFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordEntityTest {

    @Test
    void from을_통해_Record_객체로부터_RecordEntity_객체를_생성할_수_있다() {
        // given
        Record record = RecordFixture.create();

        // when
        RecordEntity recordEntity = RecordEntity.from(record);

        // then
        assertThat(recordEntity.getId()).isEqualTo(record.getId());
    }

    @Test
    void isUploader를_통해_Record_객체의_업로더가_맞는지_확인할_수_있다() {
        // given
        User user = DomainFixture.createUser();
        Record record = Record.create(new RecordCreate(
                1L,
                new FileUrl(
                        "https://www.naver.com",
                        "https://www.naver.com"
                ),
                "",
                "",
                user,
                null
        ));

        // then
        assertAll(
                () -> assertThat(record.isUploader(user.getId())).isTrue(),
                () -> assertThat(record.isUploader(100)).isFalse()
        );
    }

    @Test
    void addBookmark를_통해_Bookmark_엔티티를_bookmarks_리스트에_추가할_수_있다() {
        //given
        UserEntity userEntity = UserEntity.from(DomainFixture.createUser(2));

        RecordEntity recordEntity = new RecordEntity(
                null,
                RecordFixture.FILE_URL,
                DomainFixture.CONTENT,
                RecordFixture.EXHIBITION_NAME,
                false,
                UserEntity.from(DomainFixture.createUser()),
                PlaceEntity.create(PlaceFixture.create()),
                null,
                null
        );

        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .record(recordEntity)
                .user(userEntity)
                .build();

        //when
        recordEntity.addBookmark(bookmarkEntity);

        //then
        assertAll(
                () -> assertThat(recordEntity.getBookmarks().size()).isEqualTo(1),
                () -> assertThat(recordEntity.getBookmarks().get(0).getRecord().getId()).isEqualTo(recordEntity.getId()),
                () -> assertThat(recordEntity.getBookmarks().get(0).getUser().getId()).isEqualTo(userEntity.getId())
        );
    }

}