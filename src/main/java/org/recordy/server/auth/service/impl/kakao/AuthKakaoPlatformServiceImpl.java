package org.recordy.server.auth.service.impl.kakao;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.exception.UnauthorizedSocialTokenException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthKakaoPlatformServiceImpl implements AuthPlatformService {

    private final KakaoFeignClient kakaoFeignClient;
    private static final String TOKEN_TYPE = "Bearer ";

    public String getKakaoPlatformId(String accessToken) {
        String accessTokenWithTokenType = getAccessTokenWithTokenType(accessToken);
        KakaoAccessTokenInfo kakaoAccessTokenInfo = getKakaoAccessTokenInfo(accessTokenWithTokenType);
        return String.valueOf(kakaoAccessTokenInfo.id());
    }

    private KakaoAccessTokenInfo getKakaoAccessTokenInfo(String accessTokenWithTokenType) {
        try {
            return kakaoFeignClient.getKakaoAccessTokenInfo(accessTokenWithTokenType);
        } catch (FeignException e) {
            log.error("Feign Exception: ", e);
            throw new UnauthorizedSocialTokenException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    //인증 플랫폼 서비스 식별
    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        try {
            String platformId = getKakaoPlatformId(authSignIn.platformToken());
            return new AuthPlatform(platformId, getPlatformType());
        } catch (UnauthorizedSocialTokenException e) {
            log.error("Failed to get Kakao platform id", e);
            throw new UnauthorizedSocialTokenException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }

    private String getAccessTokenWithTokenType(String accessToken) {
        return TOKEN_TYPE + accessToken;
    }
}