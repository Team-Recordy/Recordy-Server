package org.recordy.server.slack.service;

import static org.recordy.server.slack.domain.SlackMessageBuilder.createButton;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createMarkdownField;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createSection;
import static org.recordy.server.slack.domain.SlackMessageBuilder.createVideo;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.report.domain.Report;
import org.recordy.server.report.service.ReportService;
import org.recordy.server.slack.exception.SlackException;
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

            restTemplate.postForEntity(slackApiUrl, entity, String.class);
        } catch (Exception e) {
            if (threadTs == null) {
                throw new SlackException(ErrorMessage.SLACK_SEND_FAILED);
            }
            else {
                throw new SlackException(ErrorMessage.SLACK_FEEDBACK_FAILED);
            }
        }
    }

    public static JSONArray createReport(Report report) {
        JSONArray blocks = new JSONArray();

        // 신고 정보 Section
        JSONObject section1 = new JSONObject();
        section1.put("type", "section");
        JSONArray fields1 = new JSONArray();
        fields1.put(createMarkdownField("*신고자 ID (닉네임):* " + report.getReporter().getId() + "(" + report.getReporter().getNickname() + ")"));
        fields1.put(createMarkdownField("*피신고자 ID (닉네임):* " + report.getRecord().getUser().getId() + "(" + report.getRecord().getUser().getNickname() + ")"));
        fields1.put(createMarkdownField("*장소 ID (장소명):* " + report.getRecord().getPlace().getId()  + "(" + report.getRecord().getPlace().getName() + ")"));
        fields1.put(createMarkdownField("*전시명:* " + report.getRecord().getExhibitionName()));
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

        // 레코드 본문 Section
        blocks.put(createSection("*레코드 본문:* \n" + report.getContent()));

        // 상세 신고 이유 Section
        blocks.put(createSection("*상세 신고 이유:* \n" + report.getContent()));

        // 비디오 Section
        blocks.put(createVideo("레코드 영상을 확인해보세요.", "사용자가 올린 레코드 영상입니다.", report.getRecord().getFileUrl().videoUrl(), report.getRecord().getFileUrl().thumbnailUrl()));

        // 버튼 Actions Block
        JSONObject actionsBlock = new JSONObject();
        actionsBlock.put("type", "actions");
        JSONArray elements = new JSONArray();

        elements.put(createButton("Accept", "report_accept", new JSONObject()
                .put("reportId", report.getRecord().getId())
                .put("approvalStatus", "APPROVED")));

        elements.put(createButton("Dismiss", "report_dismiss", new JSONObject()
                .put("reportId", report.getRecord().getId())
                .put("approvalStatus", "DISMISSED")));

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

