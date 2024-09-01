package org.recordy.server.exhibition.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class ExhibitionRepositoryImpl implements ExhibitionRepository {

    private final ExhibitionJpaRepository exhibitionJpaRepository;
    private final ExhibitionQueryDslRepository exhibitionQueryDslRepository;

    public Exhibition save(Exhibition exhibition) {
        ExhibitionEntity entity = exhibitionJpaRepository.save(ExhibitionEntity.from(exhibition));
        return Exhibition.from(entity);
    }

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
}
