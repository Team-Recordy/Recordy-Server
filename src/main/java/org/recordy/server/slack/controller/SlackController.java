package org.recordy.server.slack.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.report.exception.ReportException;
import org.recordy.server.report.service.ReportService;
import org.recordy.server.slack.domain.Slack;
import org.recordy.server.slack.exception.SlackException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/slack/interactive")
@RequiredArgsConstructor
public class SlackController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> handleInteractiveMessage(HttpServletRequest request) {
        try {
            Slack slack = new Slack(request);
            String actionId = slack.getActionId();

            if (actionId.contains("report")) {
                reportService.resolve(slack.getReportId(), slack.getApprovalStatus(), slack.getThreadTimestamp());
            }

            return ResponseEntity
                    .ok(null);
        } catch (ReportException |
                 SlackException e
        ) {
            throw e;
        } catch (JSONException e) {
            System.err.println("JSON parsing error: {} " + e.getMessage());
            throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);
        } catch (Exception e) {
            System.err.println("Failed to send message to Slack" + e.getMessage());
            throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);
        }
    }
}
