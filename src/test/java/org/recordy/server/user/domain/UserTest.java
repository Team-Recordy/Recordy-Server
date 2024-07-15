package org.recordy.server.user.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.controller.dto.request.TermsAgreement;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void activate를_통해_사용자의_상태를_ACTIVE로_변경할_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.PENDING);
        UserSignUp userSignUp = DomainFixture.createUserSignUp();

        // when
        User activatedUser = user.activate(userSignUp);

        // then
        assertThat(activatedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void activate를_통해_사용자_닉네임_형식이_올바르지_않은_경우_예외가_발생한다() {
        // given
        User user = DomainFixture.createUser(UserStatus.PENDING);
        UserSignUp userSignUp = new UserSignUp(
                user.getId(),
                "recordy",
                TermsAgreement.of(true, true, true)
        );

        // when, then
        assertThatThrownBy(() -> user.activate(userSignUp))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(ErrorMessage.INVALID_NICKNAME_FORMAT.getMessage());
    }

    @Test
    void activate를_통해_동의_항목에_하나라도_동의하지_않은_경우_예외가_발생한다() {
        // given
        User user = DomainFixture.createUser(UserStatus.PENDING);
        UserSignUp userSignUp = new UserSignUp(
                user.getId(),
                DomainFixture.USER_NICKNAME,
                TermsAgreement.of(false, true, true)
        );

        // when, then
        assertThatThrownBy(() -> user.activate(userSignUp))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(ErrorMessage.INVALID_REQUEST_TERM.getMessage());
    }

    @Test
    void activate를_통해_id로부터_임의의_프로필_이미지를_얻을_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.PENDING);
        UserSignUp userSignUp = DomainFixture.createUserSignUp();

        // when
        User activatedUser = user.activate(userSignUp);

        // then
        assertThat(activatedUser.getProfileImageUrl()).contains("https://recordy-bucket.s3.ap-northeast-2.amazonaws.com/profile_");
    }
}