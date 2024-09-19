package org.recordy.server.record.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.RecordFixture;
import org.recordy.server.view.domain.ViewEntity;

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
        assertAll(
                () -> assertThat(recordEntity.getId()).isEqualTo(record.getId()),
                () -> assertThat(recordEntity.getFileUrl().videoUrl()).isEqualTo(record.getFileUrl().videoUrl()),
                () -> assertThat(recordEntity.getFileUrl().thumbnailUrl()).isEqualTo(record.getFileUrl().thumbnailUrl()),
                () -> assertThat(recordEntity.getContent()).isEqualTo(record.getContent()),
                () -> assertThat(recordEntity.getUser().getId()).isEqualTo(UserEntity.from(record.getUploader()).getId())
        );
    }

    @Test
    void isUploader를_통해_Record_객체의_업로더가_맞는지_확인할_수_있다() {
        //given
        RecordEntity recordEntity = new RecordEntity(
                null,
                RecordFixture.FILE_URL,
                DomainFixture.CONTENT,
                UserEntity.from(DomainFixture.createUser()),
                PlaceEntity.create(PlaceFixture.create()),
                null,
                null
        );

        //when
        //then
        assertAll(
                () -> assertThat(Record.from(recordEntity).isUploader(DomainFixture.createUserEntity().getId())).isTrue(),
                () -> assertThat(Record.from(recordEntity).isUploader(100)).isFalse()
        );
    }

    @Test
    void addView를_통해_View_엔티티를_views_리스트에_추가할_수_있다() {
        //given
        UserEntity userEntity = DomainFixture.createUserEntity();

        RecordEntity recordEntity = new RecordEntity(
                null,
                RecordFixture.FILE_URL,
                DomainFixture.CONTENT,
                UserEntity.from(DomainFixture.createUser()),
                PlaceEntity.create(PlaceFixture.create()),
                null,
                null
        );

        ViewEntity viewEntity = ViewEntity.builder()
                .id(1L)
                .record(recordEntity)
                .user(userEntity)
                .build();

        //when
        recordEntity.addView(viewEntity);

        //then
        assertAll(
                () -> assertThat(recordEntity.getViews().size()).isEqualTo(1),
                () -> assertThat(recordEntity.getViews().get(0).getRecord().getId()).isEqualTo(recordEntity.getId()),
                () -> assertThat(recordEntity.getViews().get(0).getUser().getId()).isEqualTo(userEntity.getId())
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