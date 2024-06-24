package org.recordy.server.user.domain;

import lombok.Getter;
import org.recordy.server.auth.domain.AuthPlatform;

@Getter
public class User {

    private Long id;
    private AuthPlatform authPlatform;
    private UserStatus status;
}
