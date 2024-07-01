package org.recordy.server.auth.service.impl.kakao;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthKakaoPlatformServiceImpl implements AuthPlatformService {

    private final KakaoFeignClient kakaoFeignClient;
    private static final String TOKEN_TYPE = "Bearer ";

    //인증 플랫폼 서비스 식별
    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        try {
            String platformId = getKakaoPlatformId(authSignIn.platformToken());
            return new AuthPlatform(platformId, getPlatformType());
        } catch (AuthException e) {
            log.error("Failed to get Kakao platform id", e);
            throw new AuthException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }

    private String getKakaoPlatformId(String accessToken) {
        String accessTokenWithTokenType = getAccessTokenWithTokenType(accessToken);
        KakaoAccessPlatformInfo kakaoAccessPlatformInfo = getKakaoAccessTokenInfo(accessTokenWithTokenType);
        return String.valueOf(kakaoAccessPlatformInfo.id());
    }

    private KakaoAccessPlatformInfo getKakaoAccessTokenInfo(String accessTokenWithTokenType) {
        try {
            return kakaoFeignClient.getKakaoAccessTokenInfo(accessTokenWithTokenType);
        } catch (FeignException e) {
            log.error("Feign Exception: ", e);
            throw new AuthException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    private String getAccessTokenWithTokenType(String accessToken) {
        return TOKEN_TYPE + accessToken;
    }
}