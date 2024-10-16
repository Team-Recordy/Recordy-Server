package org.recordy.server.exhibition.service;

import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ExhibitionService {

    // command
    void create(ExhibitionCreateRequest request);
    void update(ExhibitionUpdate update);
    void delete(long exhibitionId);

    // query
    Slice<Exhibition> search(String name, Long cursor, int size);
    List<ExhibitionGetResponse> getAllByPlaceId(long placeId);
    List<ExhibitionGetResponse> getAllFreeByPlaceId(long placeId);
    List<ExhibitionGetResponse> getAllByPlaceIdOrderByEndDateDesc(long placeId);
}
