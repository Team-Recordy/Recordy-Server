package org.recordy.server.auth.service.impl.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.exception.ErrorMessage;
import org.recordy.server.auth.exception.UnauthorizedException;
import org.recordy.server.auth.kakao.KakaoOAuthProvider;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthKakaoPlatformServiceImpl implements AuthPlatformService {

    private final KakaoOAuthProvider kakaoOAuthProvider;

    //인증 플랫폼 서비스 식별
    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        try {
            String platformId = kakaoOAuthProvider.getKakaoPlatformId(authSignIn.platformToken());
            return new AuthPlatform(platformId, getPlatformType());
        } catch (UnauthorizedException e) {
            log.error("Failed to get Kakao platform id", e);
            throw new UnauthorizedException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }
}
