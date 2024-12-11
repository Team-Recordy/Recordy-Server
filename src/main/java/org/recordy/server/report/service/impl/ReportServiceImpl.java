package org.recordy.server.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.report.controller.exception.ReportException;
import org.recordy.server.report.domain.ApprovalStatus;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.domain.ReportCreate;
import org.recordy.server.report.repository.ReportRepository;
import org.recordy.server.report.service.ReportService;
import org.recordy.server.slack.service.SlackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final RecordRepository recordRepository;
    private final SlackService slackService;

    @Transactional
    @Override
    public void create(ReportCreate create) {
        checkIfReportExists(create.reporterId(), create.recordId());
        Report report = reportRepository.save(Report.create(create));
        slackService.sendFeedbackToSlack(SlackService.createReport(report), null);
        blockRecordIfExceeds(create.recordId());
    }

    private void checkIfReportExists(Long reporterId, Long recordId) {
        reportRepository.findByReporterIdAndRecordId(reporterId, recordId).ifPresent(
               report -> new ReportException(ErrorMessage.REPORT_ALREADY_EXISTS)
        );
    }

    private void blockRecordIfExceeds(Long recordId) {
        long reportSize = reportRepository.countAllByRecordIdAndCreatedAfter(recordId, LocalDateTime.now().minusDays(7));

        if (reportSize >= 5) {
            recordRepository.block(recordId);
        }
    }

    @Override
    public void resolve(Long reportId, ApprovalStatus approvalStatus, String threadTs) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportException(ErrorMessage.REPORT_NOT_FOUND)
        );
        report.resolve(approvalStatus);
        slackService.sendFeedbackToSlack(SlackService.resolveReport(report), threadTs);
    }
}
