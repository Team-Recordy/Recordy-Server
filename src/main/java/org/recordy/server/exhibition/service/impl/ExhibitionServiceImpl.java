package org.recordy.server.exhibition.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    @Transactional
    @Override
    public Exhibition create(ExhibitionCreate create) {
        return exhibitionRepository.save(Exhibition.create(create));
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
