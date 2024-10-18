package org.recordy.server.place.controller.dto.response;

import org.recordy.server.place.service.dto.google.GooglePlaceSearch;
import org.recordy.server.place.service.dto.kakao.KakaoPlaceSearch;

public record PlatformPlaceSearchResponse(
        String platformPlaceId,
        String address,
        double longitude,
        double latitude,
        String name
) {

    public static PlatformPlaceSearchResponse from(GooglePlaceSearch google) {
        return new PlatformPlaceSearchResponse(
                google.place_id(),
                google.formatted_address(),
                google.geometry().location().lng(),
                google.geometry().location().lat(),
                google.name()
        );
    }

    public static PlatformPlaceSearchResponse from(KakaoPlaceSearch kakao) {
        return new PlatformPlaceSearchResponse(
                kakao.id(),
                kakao.address_name(),
                Double.parseDouble(kakao.x()),
                Double.parseDouble(kakao.y()),
                kakao.place_name()
        );
    }
}
