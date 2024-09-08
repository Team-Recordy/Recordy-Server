package org.recordy.server.place.repository.impl;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.repository.PlaceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceQueryDslRepository placeQueryDslRepository;

    @Override
    public Place save(Place place) {
        PlaceEntity entity = placeJpaRepository.save(PlaceEntity.from(place));

        return Place.from(entity);
    }

    @Override
    public Place findById(long id) {
        PlaceEntity entity = placeQueryDslRepository.findById(id);

        if (Objects.isNull(entity)) {
            throw new PlaceException(ErrorMessage.PLACE_NOT_FOUND);
        }

        return Place.from(entity);
    }

    @Override
    public Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        return placeQueryDslRepository.findAllOrderByExhibitionStartDateDesc(pageable);
    }

    @Override
    public Slice<PlaceGetResponse> findAllFreeOrderByExhibitionStartDateDesc(Pageable pageable) {
        return placeQueryDslRepository.findAllFreeOrderByExhibitionStartDateDesc(pageable);
    }

    @Override
    public Slice<PlaceGetResponse> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query) {
        return placeQueryDslRepository.findAllByNameOrderByExhibitionStartDateDesc(pageable, query);
    }

    @Override
    public Slice<PlaceGetResponse> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance) {
        return placeQueryDslRepository.findAllByLocationOrderByExhibitionStartDateDesc(pageable, currentLocation, distance);
    }
}
