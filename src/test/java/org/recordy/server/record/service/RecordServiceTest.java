package org.recordy.server.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordServiceTest {

    private RecordService recordService;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordService = fakeContainer.recordService;
        UserRepository userRepository = fakeContainer.userRepository;

        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));
    }

    @Test
    void create을_통해_레코드를_생성할_수_있다() {
        // given
        RecordCreate recordCreate = DomainFixture.createRecordCreate();
        File file = DomainFixture.createFile();

        // when
        Record result = recordService.create(recordCreate, file);

        // then
        assertAll(
                () -> assertThat(result.getFileUrl().videoUrl()).isEqualTo(DomainFixture.VIDEO_URL),
                () -> assertThat(result.getFileUrl().thumbnailUrl()).isEqualTo(DomainFixture.THUMBNAIL_URL),
                () -> assertThat(result.getLocation()).isEqualTo(recordCreate.location()),
                () -> assertThat(result.getContent()).isEqualTo(recordCreate.content()),
                () -> assertThat(result.getUploader().getId()).isEqualTo(DomainFixture.USER_ID)
        );
    }

    @Test
    void delete을_통해_레코드를_삭제할_수_있다() {
        // given
        RecordCreate recordCreate = DomainFixture.createRecordCreate();
        File file = DomainFixture.createFile();
        Record record = recordService.create(recordCreate, file);

        // when
        recordService.delete(1, record.getId());

        // then
        Slice<RecordInfoWithBookmark> result = recordService.getRecentRecordInfoWithBookmarks(1,null, 0L, 1);
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void 업로더가_아니면_delete을_통해_레코드를_삭제할_때_예외가_발생한다() {
        // given
        RecordCreate recordCreate = DomainFixture.createRecordCreate();
        File file = DomainFixture.createFile();
        Record record = recordService.create(recordCreate, file);

        // when
        // then
        assertThatThrownBy(() -> recordService.delete(100, record.getId()))
                .isInstanceOf(RecordException.class)
                .hasMessageContaining(ErrorMessage.FORBIDDEN_DELETE_RECORD.getMessage());
    }

    @Test
    void findAllByUserIdOrderByCreatedAtDesc를_통해_userId를_기반으로_레코드_데이터를_조회할_수_있다() {
        //given
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreateByOtherUser(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreateByOtherUser(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreateByOtherUser(), DomainFixture.createFile());

        //when
        Slice<RecordInfoWithBookmark> result = recordService.getRecentRecordInfoWithBookmarksByUser(1, Long.MAX_VALUE, 10);

        //then
        assertAll(
                () -> assertThat(result.get()).hasSize(2),
                () -> assertThat(result.getContent().get(0).recordInfo().id()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(1).recordInfo().id()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getRecentRecords를_통해_커서_이후의_레코드를_최신_순서로_읽을_수_있다() {
        // given
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());

        // when
        Slice<RecordInfoWithBookmark> result = recordService.getRecentRecordInfoWithBookmarks(1, null, 6L, 10);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(5),
                () -> assertThat(result.getContent().get(0).recordInfo().id()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(1).recordInfo().id()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(2).recordInfo().id()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(3).recordInfo().id()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(4).recordInfo().id()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getRecentRecords를_통해_커서가_제일_오래된_값이라면_아무것도_반환되지_않는다() {
        // given
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());

        // when
        Slice<RecordInfoWithBookmark> result = recordService.getRecentRecordInfoWithBookmarks(1, null, 1L, 3);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }
}