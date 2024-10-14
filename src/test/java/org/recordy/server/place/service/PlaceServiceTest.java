package org.recordy.server.place.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.usecase.PlatformPlace;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.impl.PlaceServiceImpl;
import org.recordy.server.util.LocationFixture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlatformPlaceService platformPlaceService;

    @InjectMocks
    private PlaceServiceImpl sut;

    @Test
    void 구글에_API_요청을_보내_만든_Place_객체를_리포지토리에_저장한다() {
        // given
        PlatformPlace platformPlace = new PlatformPlace(
                LocationFixture.POINT,
                LocationFixture.ADDRESS,
                LocationFixture.GOOGLE_PLACE_ID,
                null
        );
        PlaceCreateRequest request = new PlaceCreateRequest(
                "CLUB FF",
                "서울"
        );
        String query = request.toQuery();

        when(platformPlaceService.getByQuery(any())).thenReturn(platformPlace);
        when(placeRepository.save(any())).thenReturn(null);

        // when
        sut.create(request);

        // then
        verify(platformPlaceService, times(1)).getByQuery(query);
        verify(placeRepository, times(1)).save(any());
    }
}