package org.recordy.server.google;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class VenueService {

    private final String CULTURE_API_KEY = "YOUR_CULTURE_API_KEY";
    private final String GOOGLE_API_KEY = "AIzaSyCQZfaMxeP2HU4gtyhkh0v5DCFsk3GiFAg";
    private final String CULTURE_API_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/area";
    private final String GOOGLE_PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";

    private final RestTemplate restTemplate;

    public VenueService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 문화공간 API로부터 장소 정보 가져오기
    public Map<String, Object> getVenueFromCultureApi(String sido, String gugun) {
        String url = UriComponentsBuilder.fromHttpUrl(CULTURE_API_URL)
                .queryParam("sido", sido)
                .queryParam("gugun", gugun)
                .queryParam("serviceKey", CULTURE_API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // Google Places API로 장소 매핑
    public Map<String, Object> findPlaceFromGoogle(String place) {
        String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACES_API_URL)
                .queryParam("input", place)
                .queryParam("inputtype", "textquery")
                .queryParam("fields", "formatted_address,name,geometry")
                .queryParam("key", GOOGLE_API_KEY)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // 전체 프로세스를 처리하여 전시회장의 주소를 구글 지도와 매핑하는 메서드
    public Map<String, Object> mapVenueToGoogle(String sido, String gugun) {
        // 1. 문화공간 API에서 전시회장 정보 가져오기
        Map<String, Object> cultureResponse = getVenueFromCultureApi(sido, gugun);

        // 전시회장 정보 추출 (필요한 데이터로 수정 가능)
        String venuePlace = extractPlaceFromCultureApi(cultureResponse);

        // 2. 구글 지도 API로 전시회장 정보 매핑
        return findPlaceFromGoogle(venuePlace);
    }

    // 문화공간 API 응답에서 장소 정보 추출
    private String extractPlaceFromCultureApi(Map<String, Object> response) {
        // 예시로 공연/전시 목록에서 첫 번째 장소를 추출
        List<Map<String, Object>> performances = (List<Map<String, Object>>) response.get("perforList");
        if (performances != null && !performances.isEmpty()) {
            return (String) performances.get(0).get("place");
        }
        return null;
    }
}



