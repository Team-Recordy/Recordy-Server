package org.recordy.server.exhibition.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class ExhibitionRepositoryImpl implements ExhibitionRepository {

    private final ExhibitionJpaRepository exhibitionJpaRepository;
    private final ExhibitionQueryDslRepository exhibitionQueryDslRepository;

    @Override
    public Exhibition save(Exhibition exhibition) {
        ExhibitionEntity entity = exhibitionJpaRepository.save(ExhibitionEntity.from(exhibition));

        return Exhibition.from(entity);
    }

    @Override
    public void saveAll(List<Exhibition> exhibitions) {
        List<ExhibitionEntity> entities = exhibitions.stream()
                .map(ExhibitionEntity::from)
                .toList();

        exhibitionJpaRepository.saveAll(entities);
    }

    @Override
    public void deleteById(long id) {
        exhibitionJpaRepository.deleteById(id);
    }

    @Override
    public Exhibition findById(long id) {
        ExhibitionEntity entity = exhibitionQueryDslRepository.findById(id);

        if (Objects.isNull(entity)) {
            throw new ExhibitionException(ErrorMessage.EXHIBITION_NOT_FOUND);
        }

        return Exhibition.from(entity);
    }

    @Override
    public Slice<Exhibition> findAllContainingName(String name, Long cursor, int size) {
        return exhibitionQueryDslRepository.findAllContainingName(name, cursor, size)
                .map(Exhibition::from);
    }

    @Override
    public List<ExhibitionGetResponse> findAllByPlaceId(long placeId) {
        return exhibitionQueryDslRepository.findAllByPlaceId(placeId);
    }

    public List<ExhibitionGetResponse> findAllFreeByPlaceId(long placeId) {
        return exhibitionQueryDslRepository.findAllFreeByPlaceId(placeId);
    }

    public List<ExhibitionGetResponse> findAllByPlaceIdOrderByEndDateDesc(long placeId) {
        return exhibitionQueryDslRepository.findAllByPlaceIdOrderByEndDateDesc(placeId);
    }
}
