package org.recordy.server.report.domain;

public record ReportCreate(
        Long reporterId,
        Long recordId,
        ReportReason reason,
        String content
) {
    public static ReportCreate of(Long reporterId,  Long recordId,
                                  ReportReason reason,
                                  String content) {
        return new ReportCreate(reporterId, recordId, reason, content);
    }
}
