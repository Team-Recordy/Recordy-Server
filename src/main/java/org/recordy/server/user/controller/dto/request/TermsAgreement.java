package org.recordy.server.user.controller.dto.request;

public record TermsAgreement(
        boolean useTerm,
        boolean personalInfoTerm,
        boolean ageTerm
) {

    public static TermsAgreement of(boolean useTerm, boolean personalInfoTerm, boolean ageTerm) {
        return new TermsAgreement(useTerm, personalInfoTerm, ageTerm);
    }
}
