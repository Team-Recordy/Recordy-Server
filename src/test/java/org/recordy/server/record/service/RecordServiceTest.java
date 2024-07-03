package org.recordy.server.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordServiceTest {

    private RecordService recordService;
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordService = fakeContainer.recordService;
        userRepository = fakeContainer.userRepository;

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
}