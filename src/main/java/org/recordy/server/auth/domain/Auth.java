package org.recordy.server.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Auth {

    private Long userId;
    private AuthPlatform platform;
    private AuthToken token;
    private boolean isSignedUp;
}
