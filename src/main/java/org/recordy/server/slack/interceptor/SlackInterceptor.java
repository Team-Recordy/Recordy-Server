package org.recordy.server.slack.interceptor;

import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.slack.exception.SlackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@RequiredArgsConstructor
public class SlackInterceptor implements HandlerInterceptor {

    @Value("${slack.signing.secret}")
    private String SLACK_SIGNING_SECRET;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // SecurityContextHolderAwareRequestWrapper가 아닌 경우에만 ContentCachingRequestWrapper로 감쌈
        ContentCachingRequestWrapper requestWrapper = (request instanceof ContentCachingRequestWrapper)
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request);

        String method = request.getMethod();
        String signature = request.getHeader("X-Slack-Signature");
        String timestamp = request.getHeader("X-Slack-Request-Timestamp");
        String payload = getRequestBody(requestWrapper);

        System.out.println("payload = " + payload);

        if (!method.equalsIgnoreCase("POST") || !isValidRequest(payload, signature, timestamp)) {
            throw new SlackException(ErrorMessage.SLACK_INVALID_REQUEST);
        }

        return true;
    }

    private String getRequestBody(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = requestWrapper.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new SlackException(ErrorMessage.FAILED_TO_READ_SLACK_REQUEST);
        }

        return stringBuilder.toString();
    }

    private boolean isValidRequest(String payload, String signature, String timestamp) {
        try {
            // Slack 검증용 문자열 생성
            String baseString = "v0:" + timestamp + ":" + payload;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(SLACK_SIGNING_SECRET.getBytes(UTF_8), "HmacSHA256");
            mac.init(secret);
            byte[] rawHmac = mac.doFinal(baseString.getBytes(UTF_8));

            // HMAC 결과값을 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(String.format("%02x", b));
            }
            String calculatedSignature = "v0=" + sb;  // 'v0='을 포함한 서명 생성

            // 서명 전체를 비교 (constant-time 비교 사용)
            return MessageDigest.isEqual(calculatedSignature.getBytes(UTF_8), signature.getBytes(UTF_8));
        } catch (Exception e) {
            throw new SlackException(ErrorMessage.FALIED_TO_MATCH_SLACK_SIGNATURE_EXCEPTION);
        }
    }
}

