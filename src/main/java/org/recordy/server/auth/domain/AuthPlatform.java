package org.recordy.server.auth.domain;

import lombok.Getter;

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
