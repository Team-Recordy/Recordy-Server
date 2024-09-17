package org.recordy.server.place.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.usecase.PlaceGoogle;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.GooglePlaceService;
import org.recordy.server.util.LocationFixture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GooglePlaceService googlePlaceService;

    @InjectMocks
    private PlaceServiceImpl sut;

    @Test
    void 구글에_API_요청을_보내_만든_Place_객체를_리포지토리에_저장한다() {
        // given
        PlaceGoogle placeGoogle = new PlaceGoogle(
                LocationFixture.POINT,
                LocationFixture.ADDRESS,
                LocationFixture.GOOGLE_PLACE_ID,
                null,
                null
        );
        PlaceCreateRequest request = new PlaceCreateRequest(
                "CLUB FF",
                "서울"
        );
        String query = request.toQuery();

        when(googlePlaceService.search(any())).thenReturn(placeGoogle);
        when(placeRepository.save(any())).thenReturn(null);

        // when
        sut.create(request);

        // then
        verify(googlePlaceService, times(1)).search(query);
        verify(placeRepository, times(1)).save(any());
    }
}