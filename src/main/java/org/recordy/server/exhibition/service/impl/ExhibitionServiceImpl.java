package org.recordy.server.exhibition.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.repository.PlaceRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    @Override
    public Exhibition create(ExhibitionCreateRequest request) {
        Place place;

        try {
            place = placeRepository.findById(request.placeId());
        } catch (PlaceException e) {
            place = null;
        }

        return exhibitionRepository.save(Exhibition.create(ExhibitionCreate.of(request, place)));
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
}
