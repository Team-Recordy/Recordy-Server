package org.recordy.server.exhibition.service;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;

public interface ExhibitionService {

    // command
    Exhibition create(ExhibitionCreate create);
    void update(ExhibitionUpdate update);
    void delete(long exhibitionId);
}
