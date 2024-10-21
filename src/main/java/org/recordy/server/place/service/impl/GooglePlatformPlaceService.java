package org.recordy.server.place.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.service.PlatformPlaceService;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.recordy.server.place.service.dto.google.GooglePlaceSearch;
import org.recordy.server.place.service.dto.google.GooglePlaceSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class GooglePlatformPlaceService implements PlatformPlaceService {

    private final String key;
    private final RestTemplate restTemplate;

    private GooglePlatformPlaceService(
            @Value("${place.google.key}") String key
    ) {
        this.key = key;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<PlatformPlaceSearchResponse> search(String query) {
        return searchGooglePlace(query).stream()
                .map(PlatformPlaceSearchResponse::from)
                .toList();
    }

    @Override
    public String searchId(String query) {
        return searchGooglePlace(query).get(0).place_id();
    }

    private List<GooglePlaceSearch> searchGooglePlace(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                .queryParam("input", query)
                .queryParam("inputtype", "textquery")
                .queryParam("fields", "formatted_address,place_id,geometry,name")
                .queryParam("language", "ko")
                .queryParam("locationbias", "circle:5000@37.566535,126.9779692")
                .queryParam("key", key)
                .build()
                .toUriString();

        List<GooglePlaceSearch> candidates = Objects.requireNonNull(restTemplate.getForObject(url, GooglePlaceSearchResponse.class)).candidates();

        if (candidates.isEmpty()) {
            log.error("{} has no search result", query);
            throw new PlaceException(ErrorMessage.PLATFORM_PLACE_NO_SEARCH_RESULT);
        }

        return candidates;
    }
}
