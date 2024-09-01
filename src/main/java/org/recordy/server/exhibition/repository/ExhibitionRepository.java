package org.recordy.server.exhibition.repository;

import org.recordy.server.exhibition.domain.Exhibition;

public interface ExhibitionRepository {

    // command
    Exhibition save(Exhibition exhibition);
    void deleteById(long id);

    // query
    Exhibition findById(long id);
}
