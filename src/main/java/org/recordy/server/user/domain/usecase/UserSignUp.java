package org.recordy.server.user.domain.usecase;

import org.recordy.server.user.domain.TermsAgreement;

public record UserSignUp(
        Long userId,
        String nickname,
        TermsAgreement termsAgreement
) {

    public static UserSignUp of(Long userId, String nickname, TermsAgreement termsAgreement) {
        return new UserSignUp(userId, nickname, termsAgreement);
    }
}
