package org.recordy.server.place.repository;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.util.ExhibitionFixture;
import org.recordy.server.util.LocationFixture;
import org.recordy.server.util.PlaceFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class GeometryPlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    void 특정_거리_안에_있는_장소_리스트를_전시_시작일의_역순으로_조회할_수_있다() {
        // given
        double distance = 4000;

        Place samePlace = placeRepository.save(PlaceFixture.create(LocationFixture.create(geometryFactory.createPoint(new Coordinate(0, 0)))));
        Place closePlace = placeRepository.save(PlaceFixture.create(LocationFixture.create(geometryFactory.createPoint(new Coordinate(0.01, 0.01)))));
        Place farPlace = placeRepository.save(PlaceFixture.create(LocationFixture.create(geometryFactory.createPoint(new Coordinate(1, 1)))));

        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now(), LocalDate.now(), samePlace));
        exhibitionRepository.save(ExhibitionFixture.create(LocalDate.now().minusDays(5), LocalDate.now(), closePlace));
        exhibitionRepository.save(ExhibitionFixture.create(farPlace));

        // when
        Slice<PlaceGetResponse> result = placeRepository.findAllByLocationOrderByExhibitionStartDateDesc(
                PageRequest.ofSize(10),
                geometryFactory.createPoint(new Coordinate(0, 0)),
                distance
        );

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(2),
                () -> assertThat(result.getContent().get(0).id()).isEqualTo(samePlace.getId()),
                () -> assertThat(result.getContent().get(1).id()).isEqualTo(closePlace.getId())
        );
    }
}
