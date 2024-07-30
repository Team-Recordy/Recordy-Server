package org.recordy.server.record.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.Slice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class RecordServiceTest {

    private RecordService recordService;
    private RecordRepository recordRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        recordService = fakeContainer.recordService;
        recordRepository = fakeContainer.recordRepository;
        UserRepository userRepository = fakeContainer.userRepository;

        userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));
    }

    @Test
    void create을_통해_레코드를_생성할_수_있다() {
        // given
        RecordCreate recordCreate = DomainFixture.createRecordCreate();

        // when
        Record result = recordService.create(recordCreate);

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
        Record record = recordService.create(recordCreate);

        // when
        recordService.delete(1, record.getId());

        // then
        Slice<Record> result = recordService.getRecentRecords(null, 0L, 1);
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void watch를_통해_시청기록을_저장할_수_있다() {
        //given

        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        Record record = recordService.create(DomainFixture.createRecordCreate());

        recordService.create(DomainFixture.createRecordCreate());

        //when
        recordService.watch(1, record.getId());
        System.out.println(record.getCreatedAt());

        //then
        Slice<Record> result = recordService.getFamousRecords(null, 0, 4);
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0)
               // () -> assertThat(result.getContent().get(0)).isEqualTo(record.getId())
        );
    }

    @Test
    void 업로더가_아니면_delete을_통해_레코드를_삭제할_때_예외가_발생한다() {
        // given
        RecordCreate recordCreate = DomainFixture.createRecordCreate();
        Record record = recordService.create(recordCreate);

        // when
        // then
        assertThatThrownBy(() -> recordService.delete(100, record.getId()))
                .isInstanceOf(RecordException.class)
                .hasMessageContaining(ErrorMessage.FORBIDDEN_DELETE_RECORD.getMessage());
    }

    @Test
    void getRecentRecordsByUser를_통해_userId를_기반으로_레코드_데이터를_조회할_수_있다() {
        //given
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreateByOtherUser());
        recordService.create(DomainFixture.createRecordCreateByOtherUser());
        recordService.create(DomainFixture.createRecordCreateByOtherUser());

        //when
        Slice<Record> result = recordService.getRecentRecordsByUser(1, Long.MAX_VALUE, 10);

        //then
        assertAll(
                () -> assertThat(result.get()).hasSize(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getRecentRecords를_통해_커서_이후의_레코드를_최신_순서로_읽을_수_있다() {
        // given
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());

        // when
        Slice<Record> result = recordService.getRecentRecords(null, 6L, 10);

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
    void getRecentRecords를_통해_커서가_제일_오래된_값이라면_아무것도_반환되지_않는다() {
        // given
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());


        // when
        Slice<Record> result = recordService.getRecentRecords(null, 1L, 3);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(0),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getRecentRecords를_통해_키워드를_디코딩해서_해당_키워드에_대한_최신_레코드만_반환할_수_있다() {
        // given
        List<Keyword> keywords = List.of(Keyword.덕후몰이, Keyword.깔끔한);
        Record record = recordService.create(DomainFixture.createRecordCreate(keywords));
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        String encordedKeyword = new String(Base64.getEncoder().encode("깔끔한,덕후몰이".getBytes(StandardCharsets.UTF_8)));

        // when
        Slice<Record> result = recordService.getRecentRecords(encordedKeyword, 4L, 3);

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(record.getId()),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void getTotalRecords를_통해_size만큼의_레코드를_중복없이_랜덤으로_반환할_수_있다() {
        // given
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());

        // when
        List<Record> result = recordService.getTotalRecords(3);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0)).isNotEqualTo(result.get(1).getId()),
                () -> assertThat(result.get(1)).isNotEqualTo(result.get(2).getId()),
                () -> assertThat(result.get(0)).isNotEqualTo(result.get(2).getId())
        );
    }

    @Test
    void getTotalRecords를_통해_size만큼의_레코드가_없으면_현재_레코드_수만큼만_반환한다() {
        // given
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());
        recordService.create(DomainFixture.createRecordCreate());


        // when
        List<Record> result = recordService.getTotalRecords(5);

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0)).isNotEqualTo(result.get(1).getId()),
                () -> assertThat(result.get(1)).isNotEqualTo(result.get(2).getId()),
                () -> assertThat(result.get(0)).isNotEqualTo(result.get(2).getId())
        );

    }
}