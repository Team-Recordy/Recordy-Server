package org.recordy.server.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.place.domain.Place;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.user.domain.User;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.RecordFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecordServiceTest extends FakeContainer {

    User user;
    RecordCreateRequest request;

    @BeforeEach
    void init() {
        user = userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));

        Place place = placeRepository.save(PlaceFixture.create());

        request = new RecordCreateRequest(
                RecordFixture.FILE_URL,
                RecordFixture.CONTENT,
                place.getId()
        );
    }

    @Test
    void create을_통해_레코드를_생성할_수_있다() {
        // given, when
        Long id = recordService.create(request, user.getId());

        // then
        Record result = recordRepository.findById(id);
        assertAll(
                () -> assertThat(result.getFileUrl().videoUrl()).isEqualTo(request.fileUrl().videoUrl()),
                () -> assertThat(result.getFileUrl().thumbnailUrl()).isEqualTo(request.fileUrl().thumbnailUrl()),
                () -> assertThat(result.getContent()).isEqualTo(request.content()),
                () -> assertThat(result.getUploader().getId()).isEqualTo(user.getId())
        );
    }

    @Test
    void delete을_통해_레코드를_삭제할_수_있다() {
        // given
        Long id = recordService.create(request, user.getId());

        // when
        recordService.delete(user.getId(), id);

        // then
        assertThat(recordRepository.findById(id)).isNull();
    }

    @Test
    void 업로더가_아니면_delete을_통해_레코드를_삭제할_때_예외가_발생한다() {
        // given
        Long id = recordService.create(request, DomainFixture.USER_ID);

        // when
        // then
        assertThatThrownBy(() -> recordService.delete(100, id))
                .isInstanceOf(RecordException.class)
                .hasMessageContaining(ErrorMessage.FORBIDDEN_DELETE_RECORD.getMessage());
    }
}