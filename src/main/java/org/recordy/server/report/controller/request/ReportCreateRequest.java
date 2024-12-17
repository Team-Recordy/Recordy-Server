package org.recordy.server.report.controller.request;

import org.recordy.server.report.domain.ReportReason;

public record ReportCreateRequest(
        Long recordId,
        ReportReason reason,
        String content
) {
}
