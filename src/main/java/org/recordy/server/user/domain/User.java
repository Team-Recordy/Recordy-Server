package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.recordy.server.user.domain.UserStatus.ACTIVE;

@AllArgsConstructor
@Builder
@Getter
public class User {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣0-9_.]+$");
    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://recordy-bucket.s3.ap-northeast-2.amazonaws.com/profile_";

    private Long id;
    private AuthPlatform authPlatform;
    private UserStatus status;
    private String profileImageUrl;
    private String nickname;
    private TermsAgreement termsAgreement;
    private LocalDateTime createdAt;

    public User activate(UserSignUp userSignUp) {
        validateNicknameFormat(userSignUp.nickname());
        validateTermsAgreed(userSignUp.termsAgreement());

        return User.builder()
                .id(this.id)
                .authPlatform(this.authPlatform)
                .status(ACTIVE)
                .profileImageUrl(getImage(id))
                .nickname(userSignUp.nickname())
                .termsAgreement(userSignUp.termsAgreement())
                .createdAt(createdAt)
                .build();
    }

    private void validateNicknameFormat(String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserException(ErrorMessage.INVALID_NICKNAME_FORMAT);
        }
    }

    private void validateTermsAgreed(TermsAgreement termsAgreement) {
        if (!termsAgreement.ageTerm() || !termsAgreement.useTerm() || !termsAgreement.personalInfoTerm()) {
            throw new UserException(ErrorMessage.INVALID_REQUEST_TERM);
        }
    }

    private String getImage(Long id) {
        return DEFAULT_PROFILE_IMAGE_URL + (id % 3);
    }
}
