package org.recordy.server.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthPlatform {

    private String id;
    private Type type;

    public enum Type {
        APPLE,
        KAKAO,
        ;
    }
}
