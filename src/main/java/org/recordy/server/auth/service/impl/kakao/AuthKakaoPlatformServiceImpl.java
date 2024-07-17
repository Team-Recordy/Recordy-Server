package org.recordy.server.auth.service.impl.kakao;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthKakaoPlatformServiceImpl implements AuthPlatformService {

    private final KakaoFeignClient kakaoFeignClient;
    @Value("${auth.token.prefix}")
    private String TOKEN_TYPE;

    @Override
    public AuthPlatform getPlatform(UserSignIn userSignIn) {
        String platformId = getKakaoPlatformId(userSignIn.platformToken());

        return new AuthPlatform(platformId, getPlatformType());
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }

    private String getKakaoPlatformId(String platformToken) {
        String accessToken = getAccessTokenWithTokenType(platformToken);
        KakaoPlatformInfo kakaoPlatformInfo = getKakaoAccessTokenInfo(accessToken);

        return kakaoPlatformInfo.id();
    }

    private KakaoPlatformInfo getKakaoAccessTokenInfo(String accessTokenWithTokenType) {
        try {
            return kakaoFeignClient.getKakaoAccessTokenInfo(accessTokenWithTokenType);
        } catch (FeignException e) {
            System.out.println("-------------------");
            System.out.println(accessTokenWithTokenType);
            log.error("Failed to get Kakao platform id", e);
            throw new AuthException(ErrorMessage.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }

    private String getAccessTokenWithTokenType(String accessToken) {
        return TOKEN_TYPE.concat(" ").concat(accessToken);
    }
}