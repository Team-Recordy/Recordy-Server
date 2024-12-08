package org.recordy.server.report.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.repository.ReportRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private final ReportJpaRepository reportJpaRepository;
    private final ReportQueryDslRepository reportQueryDslRepository;

    @Override
    public void save(Report report) {
        reportJpaRepository.save(report);
    }

    @Override
    public long countAllByRecordIdAndCreatedAfter(long recordId, LocalDateTime from) {
        return reportQueryDslRepository.countAllByRecordIdAndCreatedAfter(recordId, from);
    }
}
