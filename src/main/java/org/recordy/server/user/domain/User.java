package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.auth.domain.AuthPlatform;

@AllArgsConstructor
@Builder
@Getter
public class User {

    private Long id;
    private AuthPlatform authPlatform;
    private UserStatus status;
    private String nickname;
}
