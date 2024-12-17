package org.recordy.server.report.repository.impl;

import org.recordy.server.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
}
