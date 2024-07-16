package org.recordy.server.auth.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("auth")
public class AuthEntity {

    @Id
    private String platformId;
    private Long userId;
    private String platformType;
    private String accessToken;
    @Indexed
    private String refreshToken;
    private boolean isSignedUp;

    public static AuthEntity from(Auth auth) {
        return new AuthEntity(
                auth.getPlatform().getId(),
                auth.getUserId(),
                auth.getPlatform().getType().name(),
                auth.getToken().getAccessToken(),
                auth.getToken().getRefreshToken(),
                auth.isSignedUp()
        );
    }

    public Auth toDomain() {
        return new Auth(
                userId,
                new AuthPlatform(platformId, AuthPlatform.Type.valueOf(platformType)),
                new AuthToken(accessToken, refreshToken),
                isSignedUp
        );
    }
}
