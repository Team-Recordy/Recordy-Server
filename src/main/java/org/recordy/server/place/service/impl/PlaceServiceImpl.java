package org.recordy.server.place.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.place.domain.PlaceReview;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.place.domain.usecase.PlaceGoogle;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.repository.PlaceReviewRepository;
import org.recordy.server.place.service.GooglePlaceService;
import org.recordy.server.place.service.PlaceService;
import org.recordy.server.place.service.dto.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceReviewRepository placeReviewRepository;
    private final GooglePlaceService googlePlaceService;

    @Transactional
    @Override
    public Place create(PlaceCreateRequest request) {
        PlaceGoogle placeGoogle = googlePlaceService.search(request.toQuery());
        Location location = Location.of(placeGoogle);

        Place place = placeRepository.save(Place.create(new PlaceCreate(
                request.name(),
                location
        )));

        if (Objects.nonNull(placeGoogle.reviews())) {
            saveReviews(placeGoogle.reviews(), place);
        }

        return place;
    }
    @Override
    public Place getPlaceById(long id) {
        return placeRepository.findById(id);
    }

    @Override
    public Place getPlaceByName(String name) {
        return placeRepository.findByName(name);
    }

    @Override
    public Slice<PlaceGetResponse> getAllPlaces(Pageable pageable) {
        return placeRepository.findAllOrderByExhibitionStartDateDesc(pageable);
    }

    @Override
    public Slice<PlaceGetResponse> getFreePlaces(Pageable pageable) {
        return placeRepository.findAllFreeOrderByExhibitionStartDateDesc(pageable);
    }

    private void saveReviews(List<Review> reviews, Place place) {
        List<PlaceReview> placeReviews = reviews.stream()
                .map(review -> PlaceReview.of(review, PlaceEntity.from(place)))
                .toList();
        placeReviewRepository.saveAll(placeReviews);
    }
}
