package org.recordy.server.slack.domain;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.json.JSONObject;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.report.domain.ApprovalStatus;
import org.recordy.server.slack.exception.SlackException;

@Getter
public class Slack {

    String actionId;
    Long reportId;
    String threadTimestamp;

    ApprovalStatus approvalStatus;

    public Slack(HttpServletRequest request) {
        JSONObject json = getJsonFrom(request);

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

    private JSONObject getJsonFrom(HttpServletRequest request) {
        try {
            // HttpServletRequest에서 입력 스트림을 직접 읽기
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

            // 요청 본문을 디코딩
            String decodedPayload = URLDecoder.decode(stringBuilder.toString(), StandardCharsets.UTF_8);

            // "payload="이 포함되어 있는지 확인
            if (decodedPayload.contains("payload=")) {
                // "payload=" 뒤의 부분을 추출
                String payloadContent = decodedPayload.substring("payload=".length());
                return new JSONObject(payloadContent);
            } else {
                System.out.println("no payload");
                throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);  // "payload="이 없을 경우 예외 처리
            }
        } catch (IOException e) {
            System.out.println("io exception");
            throw new SlackException(ErrorMessage.SLACK_INTERACTION_FAILED);
        }
    }

}
