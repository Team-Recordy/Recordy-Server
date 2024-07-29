package org.recordy.server.record.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.view.domain.ViewEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordEntityTest {

    @Test
    void from을_통해_Record_객체로부터_RecordEntity_객체를_생성할_수_있다() {
        // given
        Record record = Record.builder()
                .id(DomainFixture.RECORD_ID)
                .fileUrl(new FileUrl(
                        DomainFixture.VIDEO_URL,
                        DomainFixture.THUMBNAIL_URL
                ))
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .keywords(DomainFixture.KEYWORDS)
                .uploader(DomainFixture.createUser(UserStatus.ACTIVE))
                .build();

        // when
        RecordEntity recordEntity = RecordEntity.from(record);

        // then
        assertAll(
                () -> assertThat(recordEntity.getId()).isEqualTo(record.getId()),
                () -> assertThat(recordEntity.getVideoUrl()).isEqualTo(record.getFileUrl().videoUrl()),
                () -> assertThat(recordEntity.getThumbnailUrl()).isEqualTo(record.getFileUrl().thumbnailUrl()),
                () -> assertThat(recordEntity.getLocation()).isEqualTo(record.getLocation()),
                () -> assertThat(recordEntity.getContent()).isEqualTo(record.getContent()),
                () -> assertThat(recordEntity.getUser().getId()).isEqualTo(UserEntity.from(record.getUploader()).getId())
        );
    }

    @Test
    void from을_통해_Record_객체와_관련한_키워드를_토대로_UploadEntity를_생성할_수_있다() {
        // given
        Record record = Record.builder()
                .id(1L)
                .fileUrl(new FileUrl(
                        DomainFixture.VIDEO_URL,
                        DomainFixture.THUMBNAIL_URL
                ))
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .keywords(DomainFixture.KEYWORDS)
                .uploader(DomainFixture.createUser(UserStatus.ACTIVE))
                .build();

        // when
        RecordEntity recordEntity = RecordEntity.from(record);

        // then
        recordEntity.getUploads()
                .forEach(uploadEntity -> assertThat(uploadEntity.getRecord()).isEqualTo(recordEntity));
        recordEntity.getUploads()
                .forEach(uploadEntity -> assertThat(DomainFixture.KEYWORDS).contains(uploadEntity.getKeyword().toDomain()));
    }

    @Test
    void toDomain을_통해_RecordEntity_객체로부터_Record_객체를_생성할_수_있다() {
        // given
        RecordEntity recordEntity = RecordEntity.builder()
                .videoUrl(DomainFixture.VIDEO_URL)
                .thumbnailUrl(DomainFixture.THUMBNAIL_URL)
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .user(DomainFixture.createUserEntity())
                .build();

        // when
        Record record = recordEntity.toDomain();

        // then
        assertAll(
                () -> assertThat(record.getId()).isNull(),
                () -> assertThat(record.getFileUrl().videoUrl()).isEqualTo(recordEntity.getVideoUrl()),
                () -> assertThat(record.getFileUrl().thumbnailUrl()).isEqualTo(recordEntity.getThumbnailUrl()),
                () -> assertThat(record.getLocation()).isEqualTo(recordEntity.getLocation()),
                () -> assertThat(record.getContent()).isEqualTo(recordEntity.getContent()),
                () -> assertThat(record.getUploader().getId()).isEqualTo(recordEntity.getUser().getId())
        );
    }

    @Test
    void isUploader를_통해_Record_객체의_업로더가_맞는지_확인할_수_있다() {
        //given
        RecordEntity recordEntity = RecordEntity.builder()
                .videoUrl(DomainFixture.VIDEO_URL)
                .thumbnailUrl(DomainFixture.THUMBNAIL_URL)
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .user(DomainFixture.createUserEntity())
                .build();

        //when
        //then
        assertAll(
                () -> assertThat(recordEntity.toDomain().isUploader(DomainFixture.createUserEntity().getId())).isTrue(),
                () -> assertThat(recordEntity.toDomain().isUploader(100)).isFalse()
        );
    }

    @Test
    void addUpload를_통해_업로드_엔티티를_uploads_리스트에_추가할_수_있다() {
        //given
        RecordEntity recordEntity = RecordEntity.builder()
                .videoUrl(DomainFixture.VIDEO_URL)
                .thumbnailUrl(DomainFixture.THUMBNAIL_URL)
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .user(DomainFixture.createUserEntity())
                .build();

        UploadEntity uploadEntity = UploadEntity.builder()
                .record(recordEntity)
                .keyword(KeywordEntity.from(Keyword.감각적인))
                .build();

        //when
        recordEntity.addUpload(uploadEntity);

        //then
        assertAll(
                () -> assertThat(recordEntity.getUploads().size()).isEqualTo(1),
                () -> assertThat(recordEntity.getUploads().get(0).getRecord().getId()).isEqualTo(recordEntity.getId()),
                () -> assertThat(recordEntity.getUploads().get(0).getKeyword().getKeyword()).isEqualTo(Keyword.감각적인)
        );
    }

    @Test
    void addView를_통해_View_엔티티를_views_리스트에_추가할_수_있다() {
        //given
        UserEntity userEntity = DomainFixture.createUserEntity();

        RecordEntity recordEntity = RecordEntity.builder()
                .videoUrl(DomainFixture.VIDEO_URL)
                .thumbnailUrl(DomainFixture.THUMBNAIL_URL)
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .user(DomainFixture.createUserEntity())
                .build();

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

        RecordEntity recordEntity = RecordEntity.builder()
                .videoUrl(DomainFixture.VIDEO_URL)
                .thumbnailUrl(DomainFixture.THUMBNAIL_URL)
                .location(DomainFixture.LOCATION)
                .content(DomainFixture.CONTENT)
                .user(DomainFixture.createUserEntity())
                .build();

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