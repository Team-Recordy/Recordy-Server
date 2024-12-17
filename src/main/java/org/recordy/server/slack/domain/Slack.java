package org.recordy.server.slack.domain;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.report.domain.ApprovalStatus;
import org.recordy.server.slack.exception.SlackException;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
        }
    }

    private JSONObject getJsonFrom(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            String payload = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
            String decodedPayload = URLDecoder.decode(payload, StandardCharsets.UTF_8);

            return new JSONObject(decodedPayload.substring("payload=".length()));
        } catch (UnsupportedEncodingException e) {
            throw new SlackException(ErrorMessage.USER_NOT_FOUND);
        }
    }
}
