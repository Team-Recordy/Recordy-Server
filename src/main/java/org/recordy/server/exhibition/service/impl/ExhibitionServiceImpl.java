package org.recordy.server.exhibition.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.repository.SearchRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceRepository placeRepository;
    private final SearchRepository searchRepository;

    @Transactional
    @Override
    public void create(ExhibitionCreateRequest request) {
        Place place = placeRepository.findById(request.placeId());
        Exhibition exhibition = exhibitionRepository.save(Exhibition.create(ExhibitionCreate.of(request, place)));

        searchRepository.save(Search.from(exhibition, place.getLocation().getAddress()));
    }

    @Transactional
    @Override
    public void update(ExhibitionUpdate update) {
        Exhibition updatedExhibition = exhibitionRepository.findById(update.id())
                .update(update);

        exhibitionRepository.save(updatedExhibition);
    }

    @Transactional
    @Override
    public void delete(long exhibitionId) {
        exhibitionRepository.deleteById(exhibitionId);
    }

    @Override
    public Slice<Exhibition> search(String name, Long cursor, int size) {
        return exhibitionRepository.findAllContainingName(name, cursor, size);
    }

    @Override
    public List<ExhibitionGetResponse> getAllByPlaceId(long placeId) {
        return exhibitionRepository.findAllByPlaceId(placeId);
    }

    @Override
    public List<ExhibitionGetResponse> getAllFreeByPlaceId(long placeId) {
        return exhibitionRepository.findAllFreeByPlaceId(placeId);
    }

    @Override
    public List<ExhibitionGetResponse> getAllByPlaceIdOrderByEndDateDesc(long placeId) {
        return exhibitionRepository.findAllByPlaceIdOrderByEndDateDesc(placeId);
    }
}
