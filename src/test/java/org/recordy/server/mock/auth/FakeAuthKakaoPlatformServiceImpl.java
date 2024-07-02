package org.recordy.server.mock.auth;

import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.auth.service.AuthPlatformService;

public class FakeAuthKakaoPlatformServiceImpl implements AuthPlatformService {

    private final FakeKakaoFeignClient kakaoFeignClient;

    public FakeAuthKakaoPlatformServiceImpl(FakeKakaoFeignClient kakaoFeignClient) {
        this.kakaoFeignClient = kakaoFeignClient;
    }

    @Override
    public AuthPlatform getPlatform(UserSignIn userSignIn) {
        return new AuthPlatform(
                kakaoFeignClient.getKakaoAccessTokenInfo(userSignIn.platformToken()).id(),
                getPlatformType()
        );
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }
}
