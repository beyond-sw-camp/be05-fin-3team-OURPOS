package com.ourpos.api.map;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.ourpos.api.store.Location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:config.properties")
@RequiredArgsConstructor
@Component
public class MapService {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${kakao.rest.api.key}")
    private String restApiKey;

    private final RestTemplate restTemplate;

    public Location getLocation(String addressBase) {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(apiKey)
            .build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                addressBase).await();
            if (results.length > 0) {
                double latitude = results[0].geometry.location.lat;
                double longitude = results[0].geometry.location.lng;
                log.info("위치 정보를 가져왔습니다. 위도: {}, 경도: {}", latitude, longitude);
                return new Location(latitude, longitude);
            } else {
                throw new IllegalArgumentException("위치 정보를 가져오는데 실패했습니다.");
            }
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalArgumentException("위치 정보를 가져오는데 실패했습니다.");
        }
    }

    public List<MapResponseDto> searchAddress(String addressBase) {
        Location location = getLocation(addressBase);
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + location.longitude() + "&y="
            + location.latitude();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
        );

        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());

        // ObjectMapper를 사용하여 documents 리스트를 MapResponseDto 리스트로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(
            response.getBody().get("documents"), new TypeReference<>() {
            }
        );
    }

    public Duration getDuration(Location location1, Location location2) {
        String url = "https://apis-navi.kakaomobility.com/v1/directions?" +
            "origin=" + location1.longitude() + "," + location1.latitude() +
            "&destination=" + location2.longitude() + "," + location2.latitude() +
            "&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false&alternatives=false&road_details=false";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + restApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            // 응답 로그를 출력하여 디버깅을 돕습니다.
            log.info("Full API Response: {}", body);

            // API 응답 상태 확인
            String status = (String)body.get("status");

            // 응답에서 경로 정보를 추출합니다.
            List<Map<String, Object>> routes = (List<Map<String, Object>>)body.get("routes");
            if (routes != null && !routes.isEmpty()) {
                Map<String, Object> firstRoute = routes.get(0);
                Map<String, Object> summary = (Map<String, Object>)firstRoute.get("summary");
                if (summary != null) {
                    long duration = ((Number)summary.get("duration")).longValue();
                    long distance = ((Number)summary.get("distance")).longValue();
                    log.info("Duration: {}, Distance: {}", duration, distance);
                    return new Duration(duration, distance);
                }
                log.info(firstRoute.get("result_code").toString());
                if (firstRoute.get("result_code").equals(104)) {
                    return new Duration(5L, 5L);
                }
            }
        }
        throw new RuntimeException("Failed to get a valid response from the API");
    }
}
