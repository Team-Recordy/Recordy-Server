package org.recordy.server.user.domain;

public record TermsAgreement(
        boolean useTerm,
        boolean personalInfoTerm,
        boolean ageTerm
) {

    public static TermsAgreement of(boolean useTerm, boolean personalInfoTerm, boolean ageTerm) {
        return new TermsAgreement(useTerm, personalInfoTerm, ageTerm);
    }

    public static TermsAgreement defaultAgreement() {
        return new TermsAgreement(false, false, false);
    }
}
