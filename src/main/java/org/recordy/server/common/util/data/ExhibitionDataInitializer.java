package org.recordy.server.common.util.data;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.recordy.server.common.util.data.dto.PerforList;
import org.recordy.server.common.util.data.dto.Response;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.exception.PlaceException;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.PlaceService;
import org.recordy.server.place.service.PlatformPlaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Profile({"dev"})
@Component
public class ExhibitionDataInitializer {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceService placeService;
    private final PlatformPlaceService platformPlaceService;
    private final PlaceRepository placeRepository;
    private final String key;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public ExhibitionDataInitializer(
            ExhibitionRepository exhibitionRepository,
            PlaceService placeService,
            PlatformPlaceService platformPlaceService,
            PlaceRepository placeRepository,
            @Value("${exhibition.api.key}") String key
    ) {
        this.exhibitionRepository = exhibitionRepository;
        this.placeService = placeService;
        this.platformPlaceService = platformPlaceService;
        this.placeRepository = placeRepository;
        this.key = key;
    }

    @PostConstruct
    public void init() throws Exception {
        String response = getResponse(1, 1000);
        saveExhibitions(response);
    }

    private void saveExhibitions(String apiData) throws Exception {
        List<PerforList> list = new XmlMapper().readValue(apiData, Response.class)
                .msgBody()
                .perforList()
                .stream()
                .filter(performance ->
                        LocalDate.parse(performance.endDate(), formatter).isAfter(LocalDate.now().minusDays(1)) &&
                        LocalDate.parse(performance.startDate(), formatter).isBefore(LocalDate.now().plusDays(1))
                )
                .toList();

        for (PerforList performance : list) {
            saveExhibition(performance);
        }
    }

    private void saveExhibition(PerforList performance) {
        Place place;

        try {
            place = placeRepository.findByName(performance.place());
        } catch (PlaceException e) {
            try {
                String platformId = platformPlaceService.searchId(performance.place());
                place = placeService.create(new PlaceCreateRequest(platformId));
            } catch (PlaceException ee) {
                place = null;
            }
        }

        if (Objects.nonNull(place)) {
            exhibitionRepository.save(Exhibition.create(new ExhibitionCreate(
                    null,
                    performance.title(),
                    LocalDate.parse(performance.startDate(), formatter),
                    LocalDate.parse(performance.endDate(), formatter),
                    false,
                    place
            )));
        }
    }

    private String getResponse(int page, int size) {
        try {
            URL url = new URL(UriComponentsBuilder.fromHttpUrl("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/realm")
                    .queryParam("realmCode", 6)
                    .queryParam("sido", URLEncoder.encode("서울", UTF_8))
                    .queryParam("cPage", page)
                    .queryParam("rows", size)
                    .queryParam("serviceKey", key)
                    .build()
                    .toUriString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");

            if (connection.getResponseCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
