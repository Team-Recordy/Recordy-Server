package org.recordy.server.user.controller.dto.request;

import org.recordy.server.user.domain.TermsAgreement;

public record UserSignUpRequest(
        String nickname,
        TermsAgreement termsAgreement
) {
}
