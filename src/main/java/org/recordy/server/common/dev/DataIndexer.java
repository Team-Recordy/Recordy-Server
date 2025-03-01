package org.recordy.server.common.dev;

import lombok.extern.slf4j.Slf4j;
import org.recordy.server.ServerApplication;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.search.domain.Search;
import org.recordy.server.search.repository.SearchRepository;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.WebApplicationType.NONE;

@Slf4j
public class DataIndexer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder()
                .sources(ServerApplication.class)
                .web(NONE)
                .run(args);

        index(context);
    }

    private static void index(ConfigurableApplicationContext context) {
        SearchRepository searchRepository = context.getBean(SearchRepository.class);
        PlaceRepository placeRepository = context.getBean(PlaceRepository.class);
        ExhibitionRepository exhibitionRepository = context.getBean(ExhibitionRepository.class);

        indexPlaces(placeRepository, searchRepository);
        indexExhibitions(exhibitionRepository, searchRepository);
    }

    private static void indexPlaces(
            PlaceRepository placeRepository,
            SearchRepository searchRepository
    ) {
        placeRepository.findAll().forEach(place -> {
            searchRepository.save(Search.from(place));
            log.info("indexed place: {}", place.getName());
        });
    }

    private static void indexExhibitions(
            ExhibitionRepository exhibitionRepository,
            SearchRepository searchRepository
    ) {
        exhibitionRepository.findAll().forEach(exhibition -> {
            searchRepository.save(Search.from(exhibition, exhibition.getPlace()));
            log.info("indexed exhibition: {}", exhibition.getName());
        });
    }
}
