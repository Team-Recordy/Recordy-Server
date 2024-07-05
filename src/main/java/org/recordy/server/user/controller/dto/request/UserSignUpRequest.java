package org.recordy.server.user.controller.dto.request;

public record UserSignUpRequest(
        String platformId,
        String nickname,
        boolean useTerm,
        boolean personalInfoTerm
) {
}
