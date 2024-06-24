package org.recordy.server.user.domain;

import lombok.Getter;
import org.recordy.server.auth.domain.Auth;

@Getter
public class User {

    private Long id;
    private Auth auth;
}
