package org.recordy.server.slack.domain;

import org.json.JSONObject;

public class SlackMessageBuilder {

    public static JSONObject createMarkdownField(String text) {
        return new JSONObject()
                .put("type", "mrkdwn")
                .put("text", text);
    }

    public static JSONObject createPlainText(String text) {
        return new JSONObject()
                .put("type", "plain_text")
                .put("text", text)
                .put("emoji", true);
    }

    public static JSONObject createSection(String text) {
        return new JSONObject()
                .put("type", "section")
                .put("text", new JSONObject()
                        .put("type", "mrkdwn")
                        .put("text", text));
    }

    public static JSONObject createVideoWithThumbnail(String title, String description, String videoUrl, String thumbnailUrl) {
        return new JSONObject()
                .put("type", "section")
                .put("text", new JSONObject()
                        .put("type", "mrkdwn")
                        .put("text", "*<" + videoUrl + "|" + title + ">*\n" + description))
                .put("accessory", new JSONObject()
                        .put("type", "image")
                        .put("image_url", thumbnailUrl)
                        .put("alt_text", description));
    }

    public static JSONObject createButton(String text, String actionId, JSONObject value, String style) {
        return new JSONObject()
                .put("type", "button")
                .put("text", createPlainText(text))
                .put("action_id", actionId)
                .put("value", value.toString())
                .put("style", style);
    }
}

