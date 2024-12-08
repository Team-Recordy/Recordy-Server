package org.recordy.server.report.domain;

public record ReportCreate(
        Long reporterId,
        Long recordId,
        ReportReason reason,
        String content
) {
}
