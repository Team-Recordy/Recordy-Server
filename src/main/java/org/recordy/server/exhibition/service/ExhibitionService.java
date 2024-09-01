package org.recordy.server.exhibition.service;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.springframework.data.domain.Slice;

public interface ExhibitionService {

    // command
    Exhibition create(ExhibitionCreate create);
    void update(ExhibitionUpdate update);
    void delete(long exhibitionId);

    // query
    Slice<Exhibition> search(String name, Long cursor, int size);
}
