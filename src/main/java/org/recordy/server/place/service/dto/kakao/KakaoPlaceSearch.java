package org.recordy.server.place.service.dto.kakao;

public record KakaoPlaceSearch(
        String id,
        String place_name,
        String address_name,
        String x,
        String y
) {
}
