package org.recordy.server.report.repository.impl;

import java.util.Optional;
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
    public Report save(Report report) {
        return reportJpaRepository.save(report);
    }

    @Override
    public Optional<Report> findById(long reportId) {
        return reportQueryDslRepository.findById(reportId);
    }

    @Override
    public long countAllByRecordIdAndCreatedAfter(long recordId, LocalDateTime from) {
        return reportQueryDslRepository.countAllByRecordIdAndCreatedAfter(recordId, from);
    }

    @Override
    public Optional<Report> findByReporterIdAndRecordId(long reporterId, long recordId) {
        return reportQueryDslRepository.findByReporterIdAndRecordId(reporterId, recordId);
    }
}
