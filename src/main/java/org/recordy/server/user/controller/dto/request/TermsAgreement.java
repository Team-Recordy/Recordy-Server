package org.recordy.server.user.controller.dto.request;

public record TermsAgreement(
        boolean useTerm,
        boolean personalInfoTerm,
        boolean ageTerm
) {
}
