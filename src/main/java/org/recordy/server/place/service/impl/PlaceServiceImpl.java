package org.recordy.server.place.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.repository.PlaceReviewRepository;
import org.recordy.server.place.service.PlaceService;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.repository.SearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceReviewRepository placeReviewRepository;
    private final GeometryConverter geometryConverter;
    private final SearchRepository searchRepository;

    @Transactional
    @Override
    public Place create(PlaceCreateRequest request) {
        Location location = Location.of(geometryConverter.of(request.latitude(), request.longitude()));
        Place place = placeRepository.save(Place.create(PlaceCreate.from(request, location)));

        searchRepository.save(Search.from(place));
        return place;
    }

    @Override
    public Slice<PlaceGetResponse> getAllByExhibitionStartDate(Pageable pageable) {
        return placeRepository.findAllOrderByExhibitionStartDateDesc(pageable);
    }

    @Override
    public Slice<PlaceGetResponse> getAllByGeography(Pageable pageable, double latitude, double longitude, double distance) {
        return placeRepository.findAllByLocationOrderByExhibitionStartDateDesc(pageable, geometryConverter.of(latitude, longitude), distance);
    }

    @Override
    public PlaceGetResponse getDetailById(Long id) {
        return placeRepository.findDetailById(id);
    }

    public List<PlaceReviewGetResponse> getReviewsByPlaceId(long id) {
        return placeReviewRepository.findAllByPlaceId(id);
    }
}
