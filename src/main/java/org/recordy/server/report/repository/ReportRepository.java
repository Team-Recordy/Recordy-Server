package org.recordy.server.report.repository;

import org.recordy.server.report.domain.Report;

import java.time.LocalDateTime;

public interface ReportRepository {

    // command
    void save(Report report);

    // query
    long countAllByRecordIdAndCreatedAfter(long recordId, LocalDateTime from);
}
