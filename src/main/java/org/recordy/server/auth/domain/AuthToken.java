package org.recordy.server.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthToken {

    private String accessToken;
    private String refreshToken;
}
