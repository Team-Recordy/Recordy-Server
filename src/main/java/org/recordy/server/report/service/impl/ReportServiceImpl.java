package org.recordy.server.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.domain.ReportCreate;
import org.recordy.server.report.repository.ReportRepository;
import org.recordy.server.report.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final RecordRepository recordRepository;

    @Transactional
    @Override
    public void create(ReportCreate create) {
        reportRepository.save(Report.create(create));
        blockRecordIfExceeds(create.recordId());
    }

    private void blockRecordIfExceeds(Long recordId) {
        long reportSize = reportRepository.countAllByRecordIdAndCreatedAfter(recordId, LocalDateTime.now().minusDays(7));

        if (reportSize >= 5) {
            recordRepository.block(recordId);
        }
    }
}
