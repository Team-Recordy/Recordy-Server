package org.recordy.server.user.controller.dto.request;

public record TermAgreementRequest(
        boolean useTerm,
        boolean personalInfoTerm,
        boolean ageTerm
) {
}
