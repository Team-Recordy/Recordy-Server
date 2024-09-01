package org.recordy.server.exhibition.repository;

import org.recordy.server.exhibition.domain.Exhibition;
import org.springframework.data.domain.Slice;

public interface ExhibitionRepository {

    // command
    Exhibition save(Exhibition exhibition);
    void deleteById(long id);

    // query
    Exhibition findById(long id);
    Slice<Exhibition> findAllContainingName(String name, Long cursor, int size);
}
