package org.recordy.server.report.service;

import org.recordy.server.report.domain.ReportCreate;

public interface ReportService {

    // command
    void create(ReportCreate create);
}
