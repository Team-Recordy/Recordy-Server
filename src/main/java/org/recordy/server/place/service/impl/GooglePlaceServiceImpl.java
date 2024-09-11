package org.recordy.server.place.service.impl;

import org.recordy.server.place.domain.usecase.PlaceGoogle;
import org.recordy.server.place.service.GooglePlaceService;
import org.recordy.server.place.service.dto.GooglePlaceDetails;
import org.recordy.server.place.service.dto.GooglePlaceDetailsResponse;
import org.recordy.server.place.service.dto.GooglePlaceSearch;
import org.recordy.server.place.service.dto.GooglePlaceSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GooglePlaceServiceImpl implements GooglePlaceService {

    private final String key;
    private final GeometryConverter geometryConverter;

    private GooglePlaceServiceImpl(
            @Value("${google.place.key}") String key,
            GeometryConverter geometryConverter
    ) {
        this.key = key;
        this.geometryConverter = geometryConverter;
    }

    @Override
    public PlaceGoogle search(String query) {
        GooglePlaceSearch search = getSearchResponse(query);
        GooglePlaceDetails details = getDetailsResponse(search.place_id());

        return new PlaceGoogle(
                geometryConverter.of(
                        search.geometry().location().lat(),
                        search.geometry().location().lng()
                ),
                search.formatted_address(),
                search.place_id(),
                details.reviews(),
                details.website()
        );
    }

    private GooglePlaceSearch getSearchResponse(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                .queryParam("input", query)
                .queryParam("inputtype", "textquery")
                .queryParam("fields", "geometry,formatted_address,name,place_id,rating")
                .queryParam("language", "ko")
                .queryParam("circle", "5000@37.566535,126.9779692")
                .queryParam("key", key)
                .toUriString();

        GooglePlaceSearchResponse forObject = new RestTemplate().getForObject(url, GooglePlaceSearchResponse.class);
        System.out.println("forObject.status() = " + forObject.status());
        return forObject.candidates().get(0);
    }

    private GooglePlaceDetails getDetailsResponse(String placeId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", placeId)
                .queryParam("fields", "reviews,website")
                .queryParam("language", "ko")
                .queryParam("key", key)
                .toUriString();

        return new RestTemplate().getForObject(url, GooglePlaceDetailsResponse.class).result();
    }
}
