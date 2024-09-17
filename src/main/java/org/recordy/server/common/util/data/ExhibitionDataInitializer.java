package org.recordy.server.common.util.data;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.recordy.server.common.util.data.dto.PerforList;
import org.recordy.server.common.util.data.dto.Response;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.service.PlaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Profile("dev")
@Component
public class ExhibitionDataInitializer {

    private final ExhibitionRepository exhibitionRepository;
    private final PlaceService placeService;
    private final String key;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public ExhibitionDataInitializer(
            ExhibitionRepository exhibitionRepository,
            PlaceService placeService,
            @Value("{exhibition.api.key}") String key
    ) {
        this.exhibitionRepository = exhibitionRepository;
        this.placeService = placeService;
        this.key = key;
    }

    @PostConstruct
    public void init() throws Exception {
        for (int i = 0; i < 7; i++) {
            String response = getResponse(LocalDate.EPOCH, LocalDate.of(9999, 12, 31), i, 1000);
            List<Exhibition> exhibitions = getExhibitions(response);
        }
    }

    private List<Exhibition> getExhibitions(String apiData) throws Exception {
        Response response = new XmlMapper().readValue(apiData, Response.class);
        List<PerforList> performances = response.msgBody().perforList();

        performances.stream()
                .filter(performance ->
                        LocalDate.parse(performance.endDate(), formatter).isAfter(LocalDate.now().minusDays(1)) &&
                        LocalDate.parse(performance.startDate(), formatter).isBefore(LocalDate.now().plusDays(1)) &&
                        performance.area().equals("서울")
                )
                .toList();

        return null;
    }

    private String getResponse(LocalDate from, LocalDate to, int page, int size) {
        try {
            URL url = new URL(UriComponentsBuilder.fromHttpUrl("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period")
                    .queryParam("from", from.format(formatter))
                    .queryParam("to", to.format(formatter))
                    .queryParam("cPage", page)
                    .queryParam("rows", size)
                    .queryParam("sortStdr", 1)
                    .queryParam("serviceKey", key)
                    .build()
                    .encode()
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
