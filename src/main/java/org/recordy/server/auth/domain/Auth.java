package org.recordy.server.auth.domain;

import lombok.Getter;

@Getter
public class Auth {

    private String id;
    private AuthPlatform platform;
    private AuthToken token;
    private AuthStatus status;
}
