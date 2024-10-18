package org.recordy.server.place.service.dto.kakao;

import java.util.List;

public record KakaoPlaceSearchResponse(
        List<KakaoPlaceSearch> documents
) {
}
