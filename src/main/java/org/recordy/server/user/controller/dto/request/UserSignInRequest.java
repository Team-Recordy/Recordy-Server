package org.recordy.server.user.controller.dto.request;

import org.recordy.server.auth.domain.AuthPlatform;

public record UserSignInRequest(
        AuthPlatform.Type platformType
) {
}
