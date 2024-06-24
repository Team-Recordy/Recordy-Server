package org.recordy.server.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.recordy.server.user.domain.User;

@Builder
@Getter
public class Auth {

    private String id;
    private AuthPlatform platform;
    private AuthToken token;
    private User user;
}
