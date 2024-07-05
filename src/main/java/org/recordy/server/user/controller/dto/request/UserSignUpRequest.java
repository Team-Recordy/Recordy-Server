package org.recordy.server.user.controller.dto.request;

public record UserSignUpRequest(
        Long userId,
        String nickname,
        boolean useTerm,
        boolean personalInfoTerm
) {
}
