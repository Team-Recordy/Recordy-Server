package org.recordy.server.report.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.report.controller.request.ReportCreateRequest;
import org.recordy.server.report.domain.ReportCreate;
import org.recordy.server.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> create(
            @UserId Long userId,
            @RequestBody ReportCreateRequest reportCreateRequest
    ) {
        reportService.create(ReportCreate.of(userId, reportCreateRequest.recordId(), reportCreateRequest.reason(),
                reportCreateRequest.content()));

        return ResponseEntity
                .ok()
                .build();
    }
}
