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

        if (encodedPayload == null || encodedPayload.isBlank()) {
            throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);
        }

        // URL 디코딩
        String decodedPayload = URLDecoder.decode(encodedPayload, StandardCharsets.UTF_8);
        System.out.println("Decoded Slack payload = " + decodedPayload);

        // "payload=" 제거
        String jsonPayload = decodedPayload.substring(8); // "payload=" 이후의 값 추출

        // JSON 파싱
        JSONObject json = new JSONObject(jsonPayload);

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
