package org.recordy.server.auth.domain;

public class AuthPlatform {

    private String platformId;
    private Type type;

    public enum Type {
        APPLE,
        KAKAO,
        ;
    }
}
