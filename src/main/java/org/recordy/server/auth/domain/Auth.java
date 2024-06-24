package org.recordy.server.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Auth {

    private AuthPlatform platform;
    private AuthToken token;
}
