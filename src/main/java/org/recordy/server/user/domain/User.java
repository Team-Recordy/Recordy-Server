package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.domain.usecase.UserUpdate;
import org.recordy.server.user.exception.UserException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private LocalDateTime updatedAt;

    public static User from(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .authPlatform(new AuthPlatform(entity.getPlatformId(), entity.getPlatformType()))
                .status(entity.getStatus())
                .profileImageUrl(entity.getProfileImageUrl())
                .nickname(entity.getNickname())
                .termsAgreement(TermsAgreement.of(entity.isUseTerm(), entity.isPersonalInfoTerm(), entity.isAgeTerm()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

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

    public void update(UserUpdate update) {
        if (Objects.nonNull(update.nickname())) {
            validateNicknameFormat(update.nickname());
            nickname = update.nickname();
        }

        if (Objects.nonNull(update.profileImageUrl())) {
            validateProfileImageUrl(update.profileImageUrl());
            profileImageUrl = update.profileImageUrl();
        }
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

    private void validateProfileImageUrl(String profileImageUrl) {
        try {
            new URL(profileImageUrl);
        } catch (MalformedURLException e) {
            throw new UserException(ErrorMessage.INVALID_IMAGE_URL_FORMAT);
        }
    }

    private String getImage(Long id) {
        return DEFAULT_PROFILE_IMAGE_URL + (id % 3);
    }
}
