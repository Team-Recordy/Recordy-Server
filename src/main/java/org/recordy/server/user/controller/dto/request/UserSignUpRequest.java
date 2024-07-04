package org.recordy.server.user.controller.dto.request;

public record UserSignUpRequest(
        String nickname,
        boolean useTerm,
        boolean personalInfoTerm
) {
}
