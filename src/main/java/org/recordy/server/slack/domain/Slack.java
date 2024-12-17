package org.recordy.server.slack.domain;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.json.JSONObject;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.report.domain.ApprovalStatus;
import org.recordy.server.slack.exception.SlackException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Getter
public class Slack {

    String actionId;
    Long reportId;
    String threadTimestamp;

    ApprovalStatus approvalStatus;

    public Slack(HttpServletRequest request) {
        // slackPayload 가져오기
        String encodedPayload = (String) request.getAttribute("slackPayload");
        System.out.println("Encoded Slack payload = " + encodedPayload);

        if (encodedPayload == null) {
            throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);
        }

        // URL 디코딩
        String payload = URLDecoder.decode(encodedPayload, StandardCharsets.UTF_8);
        System.out.println("Decoded Slack payload = " + payload);

        // JSON 파싱
        JSONObject json = new JSONObject(payload);

        JSONObject action = json.getJSONArray("actions").getJSONObject(0);
        JSONObject value = new JSONObject(action.getString("value"));

        actionId = action.getString("action_id");
        reportId = value.getLong("reportId");
        threadTimestamp = json.getJSONObject("container").optString("message_ts");

        if (actionId.contains("report")) {
            approvalStatus = ApprovalStatus.valueOf(value.getString("approvalStatus"));
            System.out.println("actionId = " + actionId);
            System.out.println("reportId = " + reportId);
            System.out.println("approvalStatus = " + approvalStatus);
        }
    }
}
