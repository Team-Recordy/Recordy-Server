package org.recordy.server.auth.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("auth")
public class AuthEntity extends JpaMetaInfoEntity {

    @Id
    private String platformId;
    private String platformType;
    private String accessToken;
    private String refreshToken;

    public static AuthEntity from(Auth auth) {
        return new AuthEntity(
                auth.getPlatform().getId(),
                auth.getPlatform().getType().name(),
                auth.getToken().getAccessToken(),
                auth.getToken().getRefreshToken()
        );
    }

    public Auth toDomain() {
        return new Auth(
                new AuthPlatform(platformId, AuthPlatform.Type.valueOf(platformType)),
                new AuthToken(accessToken, refreshToken)
        );
    }
}
