package org.recordy.server.report.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.report.exception.ReportException;
import org.recordy.server.report.domain.ApprovalStatus;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.domain.ReportCreate;
import org.recordy.server.report.repository.ReportRepository;
import org.recordy.server.report.service.ReportService;
import org.recordy.server.slack.service.SlackService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final RecordRepository recordRepository;
    private final SlackService slackService;

    @Transactional
    @Override
    @Synchronized
    public void create(ReportCreate create) {
        checkIfReportExists(create.reporterId(), create.recordId());
        Report report = reportRepository.save(Report.create(create));
        User reporter = userRepository.findById(create.reporterId());
        Record record = recordRepository.findById(report.getRecord().getId());
        slackService.sendFeedbackToSlack(SlackService.createReport(report, record, reporter), null);
        blockRecordIfExceeds(create.recordId());
    }

    private void checkIfReportExists(Long reporterId, Long recordId) {
        reportRepository.findByReporterIdAndRecordId(reporterId, recordId).ifPresent(
               report -> {
                   throw new ReportException(ErrorMessage.REPORT_ALREADY_EXISTS);
               }
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
                () -> {
                    throw new ReportException(ErrorMessage.REPORT_NOT_FOUND);
                }
        );
        report.resolve(approvalStatus);
        slackService.sendFeedbackToSlack(SlackService.resolveReport(report), threadTs);
    }
}
