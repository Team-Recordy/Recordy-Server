package org.recordy.server.view.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.view.domain.View;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/view-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ViewRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private ViewRepository viewRepository;

    @Test
    void save를_통해_조회_데이터를_저장할_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);
        Record record = DomainFixture.createRecord();

        // when
        View view = viewRepository.save(View.builder()
                .record(record)
                .user(user)
                .build());

        // then
        assertAll(
                () -> assertThat(view).isNotNull(),
                () -> assertThat(view.getUser().getId()).isEqualTo(user.getId()),
                () -> assertThat(view.getRecord().getId()).isEqualTo(record.getId())
        );
    }

    // TODO: ViewRepository에 대한 query 메서드 구현해야 테스트 작성 가능
//    @Test
//    void deleteByUserId를_통해_특정_사용자의_조회_데이터를_삭제할_수_있다() {
//        // given
//        long userId = 1L;
//        viewRepository.save(View.builder()
//                .record(DomainFixture.createRecord())
//                .user(DomainFixture.createUser(userId))
//                .build());
//
//        // when
//        viewRepository.deleteByUserId(userId);
//
//        // then
//        assertThat(viewRepository.countAllByUserIdGroupByKeyword(userId)).isEmpty();
//    }
}