package org.recordy.server.mock.place;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.place.repository.PlaceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.recordy.server.common.util.QueryDslUtils.hasNext;

public class FakePlaceRepository implements PlaceRepository {

    public long placeAutoIncrementId = 1L;
    private final Map<Long, Place> places = new ConcurrentHashMap<>();

    @Override
    public Place save(Place place) {
        if (Objects.nonNull(place.getId())) {
            places.put(place.getId(), place);

            return place;
        }

        PlaceCreate create = new PlaceCreate(
                placeAutoIncrementId,
                place.getName(),
                place.getLocation()
        );

        Place realPlace = Place.create(create);
        places.put(placeAutoIncrementId++, realPlace);

        return realPlace;
    }

    @Override
    public Place findById(long id) {
        return places.get(id);
    }

    @Override
    public Slice<Place> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<Place> content = places.values().stream()
                .filter(place -> Objects.nonNull(place.getExhibitions()))
                .filter(place -> place.getExhibitions().stream().anyMatch(exhibition -> exhibition.getEndDate().isAfter(exhibition.getStartDate())))
                .sorted((place1, place2) -> place2.getExhibitions().stream()
                        .map(Exhibition::getStartDate)
                        .max(LocalDate::compareTo)
                        .orElseThrow().compareTo(
                                place1.getExhibitions().stream()
                                        .map(Exhibition::getStartDate)
                                        .max(LocalDate::compareTo)
                                        .orElseThrow()
                        )
                )
                .toList()
                .subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize());

        if (content.size() < pageable.getPageSize()) {
            return new SliceImpl<>(content, pageable, false);
        }

        return new SliceImpl<>(content, pageable, hasNext(pageable, content));
    }

    @Override
    public Slice<Place> findAllFreeOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<Place> content = places.values().stream()
                .filter(place -> Objects.nonNull(place.getExhibitions()))
                .filter(place -> place.getExhibitions().stream().anyMatch(Exhibition::isFree))
                .filter(place -> place.getExhibitions().stream().anyMatch(exhibition -> exhibition.getEndDate().isAfter(exhibition.getStartDate())))
                .sorted((place1, place2) -> place2.getExhibitions().stream()
                        .map(Exhibition::getStartDate)
                        .max(LocalDate::compareTo)
                        .orElseThrow().compareTo(
                                place1.getExhibitions().stream()
                                        .map(Exhibition::getStartDate)
                                        .max(LocalDate::compareTo)
                                        .orElseThrow()
                        )
                )
                .toList()
                .subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize());

        if (content.size() < pageable.getPageSize()) {
            return new SliceImpl<>(content, pageable, false);
        }

        return new SliceImpl<>(content, pageable, hasNext(pageable, content));
    }

    @Override
    public Slice<Place> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query) {
        List<Place> content = places.values().stream()
                .filter(place -> Objects.nonNull(place.getExhibitions()))
                .filter(place -> place.getName().contains(query))
                .filter(place -> place.getExhibitions().stream().anyMatch(exhibition -> exhibition.getEndDate().isAfter(exhibition.getStartDate())))
                .sorted((place1, place2) -> place2.getExhibitions().stream()
                        .map(Exhibition::getStartDate)
                        .max(LocalDate::compareTo)
                        .orElseThrow().compareTo(
                                place1.getExhibitions().stream()
                                        .map(Exhibition::getStartDate)
                                        .max(LocalDate::compareTo)
                                        .orElseThrow()
                        )
                )
                .toList()
                .subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize());

        if (content.size() < pageable.getPageSize()) {
            return new SliceImpl<>(content, pageable, false);
        }

        return new SliceImpl<>(content, pageable, hasNext(pageable, content));
    }
}
