package org.recordy.server.auth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(name = "${oauth.kakao.name}", url = "${oauth.kakao.url}")
@FeignClient(name = "kakao-feign-client", url = "https://kapi.kakao.com/")
public interface KakaoFeignClient {
    @GetMapping("v1/user/access_token_info")
    KakaoAccessTokenInfo getKakaoAccessTokenInfo(@RequestHeader("Authorization") String accessToken);
}
