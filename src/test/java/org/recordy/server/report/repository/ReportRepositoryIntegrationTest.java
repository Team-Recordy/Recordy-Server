package org.recordy.server.report.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.report.domain.Report;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.RecordFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;
@SqlGroup({
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/report-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
public class ReportRepositoryIntegrationTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private ReportRepository reportRepository;

    @Test
    void save를_통해_신고_데이터를_저장할_수_있다() {
        // given
        recordRepository.save(RecordFixture.create(PlaceFixture.create(1)));

        // when
        Report report = reportRepository.save(DomainFixture.createReport());

        // then
        Record result = recordRepository.findById(report.getId());
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 신고_id로_신고를_조회할_수_있다() {
        // given
        recordRepository.save(RecordFixture.create(PlaceFixture.create(1)));
        Report report = reportRepository.save(DomainFixture.createReport());

        // when
        Record result = recordRepository.findById(report.getId());
        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(report.getId())
        );
    }

    @Test
    void countAllByRecordIdAndCreatedAfter로_특정_레코드에_신고_몇_번이_들어갔는지_확인할_수_있다() {
        // when
        //record2가 user1,user2에게 각각 한번씩 신고당한 상황
        Long count = reportRepository.countAllByRecordIdAndCreatedAfter(2, LocalDateTime.now().minusDays(1));

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByReporterIdAndRecordId로_특정_사용자가_특정_레코드에_넣은_신고를_조회할_수_있다() {
        // when
        //record2가 user1,user2에게 각각 한번씩 신고당한 상황
        Optional<Report> report = reportRepository.findByReporterIdAndRecordId(1,2);

        // then
        assertAll(
                () -> assertThat(report).isPresent(),
                () -> assertThat(report.get().getReporter().getId()).isEqualTo(1),
                () -> assertThat(report.get().getRecord().getId()).isEqualTo(2)
        );
    }
}
