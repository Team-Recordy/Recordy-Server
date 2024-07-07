package org.recordy.server.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordServiceTest {

    private RecordService recordService;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordService = fakeContainer.recordService;
        UserRepository userRepository = fakeContainer.userRepository;

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
    void getRecentRecordsLaterThanCursor를_통해_커서_이후의_레코드를_최신_순서로_읽을_수_있다() {
        // given
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());

        // when
        Slice<Record> result = recordService.getRecentRecordsLaterThanCursor(6, 10);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(5),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(5L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(4L),
                () -> assertThat(result.getContent().get(2).getId()).isEqualTo(3L),
                () -> assertThat(result.getContent().get(3).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(4).getId()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getRecentRecordsLaterThanCursorByUser를_통해_커서가_제일_오래된_값이라면_아무것도_반환되지_않는다() {
        // given
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());
        recordService.create(DomainFixture.createRecordCreate(), DomainFixture.createFile());

        // when
        Slice<Record> result = recordService.getRecentRecordsLaterThanCursor(1, 3);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }
}