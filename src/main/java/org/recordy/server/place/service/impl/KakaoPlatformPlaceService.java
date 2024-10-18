package org.recordy.server.place.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.service.PlatformPlaceService;
import org.recordy.server.place.service.dto.kakao.KakaoPlaceSearch;
import org.recordy.server.place.service.dto.kakao.KakaoPlaceSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@Primary
@Slf4j
@Component
public class KakaoPlatformPlaceService implements PlatformPlaceService {

    private final String key;
    private final double longitude;
    private final double latitude;
    private final int radius;
    private final RestTemplate restTemplate;

    public KakaoPlatformPlaceService(
            @Value("${place.kakao.key}") String key,
            @Value("${place.kakao.longitude}") double longitude,
            @Value("${place.kakao.latitude}") double latitude,
            @Value("${place.kakao.radius}") int radius
    ) {
        this.key = key;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<PlatformPlaceSearchResponse> search(String query) {
        return searchKakaoPlaces(query).stream()
                .map(PlatformPlaceSearchResponse::from)
                .toList();
    }

    @Override
    public String searchId(String query) {
        return searchKakaoPlaces(query).get(0).id();
    }

    private List<KakaoPlaceSearch> searchKakaoPlaces(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword")
                .queryParam("query", query)
                .queryParam("y", latitude)
                .queryParam("x", longitude)
                .queryParam("radius", radius)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "KakaoAK " + key);

        ResponseEntity<KakaoPlaceSearchResponse> response = restTemplate.exchange(url, GET, new HttpEntity<>(headers), KakaoPlaceSearchResponse.class);
        List<KakaoPlaceSearch> result = Objects.requireNonNull(response.getBody()).documents();

        if (result.isEmpty()) {
            log.error("{} has no search result", query);
            throw new PlaceException(ErrorMessage.PLATFORM_PLACE_NO_SEARCH_RESULT);
        }

        return result;
    }
}
