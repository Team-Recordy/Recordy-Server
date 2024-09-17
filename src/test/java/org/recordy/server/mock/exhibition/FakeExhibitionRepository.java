package org.recordy.server.mock.exhibition;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.exception.ExhibitionException;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Comparator;
import java.util.List;
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
                exhibition.getEndDate(),
                exhibition.isFree(),
                exhibition.getUrl(),
                exhibition.getPlace()
        );
        Exhibition realExhibition = Exhibition.create(create);
        exhibitions.put(exhibitionAutoIncrementId++, realExhibition);

        return realExhibition;
    }

    @Override
    public void saveAll(List<Exhibition> exhibitions) {

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

    @Override
    public Slice<Exhibition> findAllContainingName(String name, Long cursor, int size) {
        List<Exhibition> content = exhibitions.values().stream()
                .filter(exhibition -> exhibition.getName().contains(name) && (Objects.isNull(cursor) || exhibition.getId() < cursor))
                .sorted(Comparator.comparing(Exhibition::getId).reversed())
                .toList();

        if (content.size() < size)
            return new SliceImpl<>(content, PageRequest.ofSize(size), false);

        return new SliceImpl<>(content.subList(0, size), PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }
}
