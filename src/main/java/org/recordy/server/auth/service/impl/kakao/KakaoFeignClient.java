package org.recordy.server.auth.service.impl.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${oauth.kakao.name}", url = "${oauth.kakao.url}")
public interface KakaoFeignClient {
    @GetMapping("v1/user/access_token_info")
    KakaoAccessTokenInfo getKakaoAccessTokenInfo(@RequestHeader("Authorization") String accessToken);
}
