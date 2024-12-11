package org.recordy.server.slack.service;

import static org.recordy.server.slack.domain.SlackMessageBuilder.createButton;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createMarkdownField;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createSection;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createVideoWithThumbnail;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.Record;
import org.recordy.server.report.domain.Report;
import org.recordy.server.slack.exception.SlackException;
import org.recordy.server.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${slack.token}")
    private String token;

    @Value("${slack.channel.id}")
    private String channelId;

    public void sendFeedbackToSlack(JSONArray blocks, String threadTs) {
        try {
            JSONObject responseJson = new JSONObject();
            responseJson.put("channel", channelId);
            responseJson.put("blocks", blocks);

            // 스레드에 답장할 경우 thread_ts 포함
            if (threadTs != null && !threadTs.isEmpty()) {
                responseJson.put("thread_ts", threadTs);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(responseJson.toString(), headers);
            String slackApiUrl = "https://slack.com/api/chat.postMessage";

            String response = restTemplate.postForObject(slackApiUrl, entity, String.class);

            responseJson = new JSONObject(response);
            boolean isSuccess = responseJson.optBoolean("ok", false);

            if (!isSuccess) {
                String errorMessage = responseJson.optString("error", "알 수 없는 오류");
                System.out.println("Slack API 오류: " + errorMessage); // 오류 메시지 로그
                throw new SlackException(ErrorMessage.SLACK_SEND_FAILED);
            }
        } catch (Exception e) {
            System.out.println("dndfndsoifnsdoifhsd");
            if (threadTs == null) {
                throw new SlackException(ErrorMessage.SLACK_SEND_FAILED);
            }
            else {
                throw new SlackException(ErrorMessage.SLACK_FEEDBACK_FAILED);
            }
        }
    }

    public static JSONArray createReport(Report report, Record record, User reporter) {
        JSONArray blocks = new JSONArray();

        // 신고 정보 Section
        JSONObject section1 = new JSONObject();
        section1.put("type", "section");
        JSONArray fields1 = new JSONArray();
        fields1.put(createMarkdownField("*신고자 ID (닉네임):* " + reporter.getId() + "(" + reporter.getNickname() + ")"));
        fields1.put(createMarkdownField("*피신고자 ID (닉네임):* " + record.getUploader().getId() + "(" + record.getUploader().getNickname() + ")"));
        fields1.put(createMarkdownField("*장소 ID (장소명):* " + record.getPlace().getId()  + "(" + record.getPlace().getName() + ")"));
        fields1.put(createMarkdownField("*전시명:* " + record.getExhibitionName()));
        section1.put("fields", fields1);
        blocks.put(section1);

        // 추가 정보 Section
        JSONObject section2 = new JSONObject();
        section2.put("type", "section");
        JSONArray fields2 = new JSONArray();
        fields2.put(createMarkdownField("*신고 ID:* " + report.getId()));
        fields2.put(createMarkdownField("*신고 이유:* " + report.getReason()));
        fields2.put(createMarkdownField("*신고 일시:* " + report.getCreatedAt()));
        section2.put("fields", fields2);
        blocks.put(section2);

        // 상세 신고 이유 Section
        blocks.put(createSection("*상세 신고 이유:* \n" + report.getContent()));

        // 비디오 Section
        blocks.put(createVideoWithThumbnail("레코드 영상을 확인하려면 클릭하세요.", "*레코드 본문:* " + record.getContent(), record.getFileUrl().videoUrl(), record.getFileUrl().thumbnailUrl()));

        // 버튼 Actions Block
        JSONObject actionsBlock = new JSONObject();
        actionsBlock.put("type", "actions");
        JSONArray elements = new JSONArray();

        elements.put(createButton("Accept", "report_accept", new JSONObject()
                .put("reportId", report.getRecord().getId())
                .put("approvalStatus", "APPROVED"), "danger"));

        elements.put(createButton("Dismiss", "report_dismiss", new JSONObject()
                .put("reportId", report.getRecord().getId())
                .put("approvalStatus", "DISMISSED"), "primary"));

        actionsBlock.put("elements", elements);
        blocks.put(actionsBlock);

        return blocks;

    }

    public static JSONArray resolveReport(Report report) {
        JSONArray blocks = new JSONArray();

        blocks.put(createSection(
                ":white_check_mark: *승인 여부*: " + report.getApprovalStatus()
        ));

        return blocks;
    }


}

