package org.recordy.server.mock.auth;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.recordy.server.auth.service.impl.kakao.KakaoFeignClient;
import org.recordy.server.auth.service.impl.kakao.KakaoPlatformInfo;
import org.recordy.server.util.DomainFixture;

import java.util.Map;

public class FakeKakaoFeignClient implements KakaoFeignClient {

    @Override
    public KakaoPlatformInfo getKakaoAccessTokenInfo(String accessToken) {

        if (!accessToken.equals(DomainFixture.PLATFORM_TOKEN) && !accessToken.equals("root")) {
            throw new FeignException.Unauthorized("Unauthorized",
                    Request.create(
                            Request.HttpMethod.GET,
                            "http://localhost:8080/v1/user/access_token_info",
                            Map.of(),
                            null,
                            null,
                            new RequestTemplate()
                    ),
                    null,
                    null
            );
        }

        return new KakaoPlatformInfo(DomainFixture.PLATFORM_ID);
    }
}
