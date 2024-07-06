package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.controller.dto.request.TermsAgreement;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;

import java.util.regex.Pattern;

import static org.recordy.server.user.domain.UserStatus.ACTIVE;

@AllArgsConstructor
@Builder
@Getter
public class User {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣0-9_.]+$");

    private Long id;
    private AuthPlatform authPlatform;
    private UserStatus status;
    private String nickname;
    private TermsAgreement termsAgreement;

    public User activate(UserSignUp userSignUp) {
        validateNicknameFormat(userSignUp.nickname());
        validateTermsAgreed(userSignUp.termsAgreement());

        return User.builder()
                .id(this.id)
                .authPlatform(this.authPlatform)
                .nickname(userSignUp.nickname())
                .status(ACTIVE)
                .termsAgreement(userSignUp.termsAgreement())
                .build();
    }

    private void validateNicknameFormat(String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserException(ErrorMessage.INVALID_NICKNAME_FORMAT);
        }
    }

    private void validateTermsAgreed(TermsAgreement termsAgreement) {
        if (termsAgreement.ageTerm() && termsAgreement.useTerm() && termsAgreement.personalInfoTerm()) {
            throw new UserException(ErrorMessage.INVALID_REQUEST_TERM);
        }
    }
}
