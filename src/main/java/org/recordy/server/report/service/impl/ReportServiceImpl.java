package org.recordy.server.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.domain.ReportCreate;
import org.recordy.server.report.repository.ReportRepository;
import org.recordy.server.report.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    @Override
    public void create(ReportCreate create) {
        reportRepository.save(Report.create(create));
    }
}
