package org.recordy.server.user.controller.dto.request;

import org.junit.jupiter.api.Test;
import org.recordy.server.user.domain.TermsAgreement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TermsAgreementTest {

    @Test
    void of를_통해_각_항목에_대한_boolean_변수로부터_TermsAgreement_객체를_생성할_수_있다() {
        // given
        boolean useTerm = true;
        boolean personalInfoTerm = true;
        boolean ageTerm = false;

        // when
        TermsAgreement termsAgreement = TermsAgreement.of(useTerm, personalInfoTerm, ageTerm);

        // then
        assertAll(
                () -> assertThat(termsAgreement.useTerm()).isEqualTo(useTerm),
                () -> assertThat(termsAgreement.personalInfoTerm()).isEqualTo(personalInfoTerm),
                () -> assertThat(termsAgreement.ageTerm()).isEqualTo(ageTerm)
        );
    }

    @Test
    void defaultAgreement를_통해_모든_항목이_false인_TermsAgreement_객체를_생성할_수_있다() {
        // given, when
        TermsAgreement termsAgreement = TermsAgreement.defaultAgreement();

        // then
        assertAll(
                () -> assertThat(termsAgreement.useTerm()).isFalse(),
                () -> assertThat(termsAgreement.personalInfoTerm()).isFalse(),
                () -> assertThat(termsAgreement.ageTerm()).isFalse()
        );
    }
}