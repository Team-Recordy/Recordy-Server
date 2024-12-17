package org.recordy.server.report.domain;

public enum ReportReason {

    SPAM_PROMOTION("스팸홍보/도배입니다."),
    OBSCENE_CONTENT("음란물입니다."),
    ILLEGAL_CONTENT("불법정보를 포함하고 있습니다."),
    HATE_SPEECH("욕설/생명경시/혐오/차별적인 표현입니다."),
    PERSONAL_INFO("개인정보가 노출되었습니다."),
    OFFENDING_CONTENT("불쾌한 표연이 있습니다."),
    OTHER("기타"),
    ;

    private final String description;

    ReportReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
