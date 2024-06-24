package org.recordy.server.auth.domain;

import lombok.Getter;

@Getter
public class AuthToken {

    private String accessToken;
    private String refreshToken;
}
