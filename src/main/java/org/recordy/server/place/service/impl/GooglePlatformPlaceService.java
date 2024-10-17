package org.recordy.server.place.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.domain.usecase.PlatformPlace;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.service.PlatformPlaceService;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.recordy.server.place.service.dto.google.GooglePlaceDetails;
import org.recordy.server.place.service.dto.google.GooglePlaceDetailsResponse;
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
    private final GeometryConverter geometryConverter;
    private final RestTemplate restTemplate;

    private GooglePlatformPlaceService(
            @Value("${google.place.key}") String key,
            GeometryConverter geometryConverter
    ) {
        this.key = key;
        this.geometryConverter = geometryConverter;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public PlatformPlace getById(String id) {
        GooglePlaceDetails details = getGooglePlaceDetails(id);

        return PlatformPlace.of(
                geometryConverter.of(
                        details.geometry().location().lat(),
                        details.geometry().location().lng()
                ),
                details
        );
    }

    @Override
    public String searchId(String query) {
        return searchGooglePlace(query).get(0).place_id();
    }

    @Override
    public List<PlatformPlaceSearchResponse> search(String query) {
        return searchGooglePlace(query).stream()
                .map(PlatformPlaceSearchResponse::from)
                .toList();
    }

    private List<GooglePlaceSearch> searchGooglePlace(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                .queryParam("input", query)
                .queryParam("inputtype", "textquery")
                .queryParam("fields", "formatted_address,place_id")
                .queryParam("language", "ko")
                .queryParam("locationbias", "circle:5000@37.566535,126.9779692")
                .queryParam("key", key)
                .build()
                .toUriString();

        List<GooglePlaceSearch> candidates = Objects.requireNonNull(restTemplate.getForObject(url, GooglePlaceSearchResponse.class)).candidates();

        if (candidates.isEmpty()) {
            log.error("{} has no search result", query);
            throw new PlaceException(ErrorMessage.PLACE_GOOGLE_NO_RESULT);
        }

        return candidates;
    }

    private GooglePlaceDetails getGooglePlaceDetails(String placeId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", placeId)
                .queryParam("fields", "geometry,formatted_address,name,place_id,rating,reviews")
                .queryParam("language", "ko")
                .queryParam("key", key)
                .build()
                .toUriString();

        return Objects.requireNonNull(restTemplate.getForObject(url, GooglePlaceDetailsResponse.class))
                .result();
    }
}
