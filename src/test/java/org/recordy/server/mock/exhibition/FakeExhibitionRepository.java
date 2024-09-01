package org.recordy.server.mock.exhibition;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.exhibition.repository.ExhibitionRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FakeExhibitionRepository implements ExhibitionRepository {

    public long exhibitionAutoIncrementId = 1L;
    private final Map<Long, Exhibition> exhibitions = new ConcurrentHashMap<>();

    @Override
    public Exhibition save(Exhibition exhibition) {
        if (Objects.nonNull(exhibition.getId())) {
            exhibitions.put(exhibition.getId(), exhibition);

            return exhibition;
        }

        ExhibitionCreate create = new ExhibitionCreate(
                exhibitionAutoIncrementId,
                exhibition.getName(),
                exhibition.getStartDate(),
                exhibition.getEndDate()
        );
        Exhibition realExhibition = Exhibition.create(create);
        exhibitions.put(exhibitionAutoIncrementId++, realExhibition);

        return realExhibition;
    }

    @Override
    public void deleteById(long id) {
        exhibitions.remove(id);
    }

    @Override
    public Exhibition findById(long id) {
        Exhibition exhibition = exhibitions.get(id);

        if (Objects.isNull(exhibition)) {
            throw new ExhibitionException(ErrorMessage.EXHIBITION_NOT_FOUND);
        }

        return exhibition;
    }
}
