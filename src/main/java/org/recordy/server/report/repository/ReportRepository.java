package org.recordy.server.report.repository;

import java.util.Optional;
import org.recordy.server.report.domain.Report;

import java.time.LocalDateTime;

public interface ReportRepository {

    // command
    Report save(Report report);

    // query
    Optional<Report> findById(long reportId);
    long countAllByRecordIdAndCreatedAfter(long recordId, LocalDateTime from);
    Optional<Report> findByReporterIdAndRecordId(long reporterId, long recordId);
}
