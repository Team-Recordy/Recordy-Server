package org.recordy.server.report.repository;

import org.recordy.server.report.domain.Report;

public interface ReportRepository {

    // command
    void save(Report report);
}
