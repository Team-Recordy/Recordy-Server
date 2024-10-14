package org.recordy.server.exhibition.repository;

import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.Exhibition;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ExhibitionRepository {

    // command
    Exhibition save(Exhibition exhibition);
    void saveAll(List<Exhibition> exhibitions);
    void deleteById(long id);

    // query
    Exhibition findById(long id);
    Slice<Exhibition> findAllContainingName(String name, Long cursor, int size);
    List<ExhibitionGetResponse> findAllByPlaceId(long placeId);
    List<ExhibitionGetResponse> findAllFreeByPlaceId(long placeId);
    List<ExhibitionGetResponse> findAllByPlaceIdOrderByEndDateDesc(long placeId);
}
