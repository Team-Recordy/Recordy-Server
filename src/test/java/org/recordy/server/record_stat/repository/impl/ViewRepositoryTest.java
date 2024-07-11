package org.recordy.server.record_stat.repository.impl;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.record_stat.repository.ViewRepository;
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
        @Sql(value = "/sql/view-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/view-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
}