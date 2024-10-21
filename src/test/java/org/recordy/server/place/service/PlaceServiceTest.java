package org.recordy.server.place.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.impl.GeometryConverter;
import org.recordy.server.place.service.impl.PlaceServiceImpl;
import org.recordy.server.search.repository.SearchRepository;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GeometryConverter geometryConverter;

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private PlaceServiceImpl sut;

    @Test
    void 플랫폼_id로부터_정보를_가져와_장소_객체를_생성한다() {
        // given
        PlaceCreateRequest request = PlaceFixture.createRequest;

        when(geometryConverter.of(anyDouble(), anyDouble())).thenReturn(LocationFixture.POINT);
        when(placeRepository.save(any())).thenReturn(PlaceFixture.create(1));

        // when
        sut.create(request);

        // then
        verify(geometryConverter, times(1)).of(anyDouble(), anyDouble());
        verify(placeRepository, times(1)).save(any());
    }

    @Test
    void 장소_객체를_저장할_때_검색_객체를_함께_인덱싱한다() {
        // given
        PlaceCreateRequest request = PlaceFixture.createRequest;

        when(geometryConverter.of(anyDouble(), anyDouble())).thenReturn(LocationFixture.POINT);
        when(placeRepository.save(any())).thenReturn(PlaceFixture.create(1));

        // when
        sut.create(request);

        // then
        verify(searchRepository, times(1)).save(any());
    }
}