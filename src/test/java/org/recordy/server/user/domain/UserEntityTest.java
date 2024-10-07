package org.recordy.server.user.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserEntityTest {

    @Test
    void from을_통해_User_객체로부터_UserEntity_객체를_생성할_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);

        // when
        UserEntity userEntity = UserEntity.from(user);

        // then
        assertAll(
                () -> assertThat(userEntity.getId()).isEqualTo(user.getId()),
                () -> assertThat(userEntity.getPlatformId()).isEqualTo(user.getAuthPlatform().getId()),
                () -> assertThat(userEntity.getPlatformType()).isEqualTo(user.getAuthPlatform().getType()),
                () -> assertThat(userEntity.getStatus()).isEqualTo(user.getStatus()),
                () -> assertThat(userEntity.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl()),
                () -> assertThat(userEntity.getNickname()).isEqualTo(user.getNickname()),
                () -> assertThat(userEntity.isUseTerm()).isEqualTo(user.getTermsAgreement().useTerm()),
                () -> assertThat(userEntity.isPersonalInfoTerm()).isEqualTo(user.getTermsAgreement().personalInfoTerm()),
                () -> assertThat(userEntity.isAgeTerm()).isEqualTo(user.getTermsAgreement().ageTerm())
        );
    }

    @Test
    void from을_통해_넘어온_User_객체의_id_필드가_null일_수_있다() {
        // given
        User user = User.builder()
                .id(null)
                .authPlatform(DomainFixture.createAuthPlatform())
                .status(UserStatus.ACTIVE)
                .nickname(DomainFixture.USER_NICKNAME)
                .termsAgreement(TermsAgreement.of(
                        DomainFixture.USE_TERM_AGREEMENT,
                        DomainFixture.PERSONAL_INFO_TERM_AGREEMENT,
                        DomainFixture.AGE_TERM_AGREEMENT
                ))
                .build();

        // when
        UserEntity userEntity = UserEntity.from(user);

        // then
        assertAll(
                () -> assertThat(userEntity.getId()).isEqualTo(null),
                () -> assertThat(userEntity.getPlatformId()).isEqualTo(user.getAuthPlatform().getId()),
                () -> assertThat(userEntity.getPlatformType()).isEqualTo(user.getAuthPlatform().getType()),
                () -> assertThat(userEntity.getStatus()).isEqualTo(user.getStatus()),
                () -> assertThat(userEntity.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl()),
                () -> assertThat(userEntity.getNickname()).isEqualTo(user.getNickname()),
                () -> assertThat(userEntity.isUseTerm()).isEqualTo(user.getTermsAgreement().useTerm()),
                () -> assertThat(userEntity.isPersonalInfoTerm()).isEqualTo(user.getTermsAgreement().personalInfoTerm()),
                () -> assertThat(userEntity.isAgeTerm()).isEqualTo(user.getTermsAgreement().ageTerm())
        );
    }
}