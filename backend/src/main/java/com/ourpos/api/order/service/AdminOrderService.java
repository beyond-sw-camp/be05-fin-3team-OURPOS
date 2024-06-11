package com.ourpos.api.order.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.ourpos.api.order.dto.response.CountMonthlyResponseDto;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.dto.response.MealTimeResponseDto;
import com.ourpos.api.order.dto.response.MealTypeResponseDto;
import com.ourpos.api.order.dto.response.MenuPreferResponseDto;
import com.ourpos.api.store.Location;
import com.ourpos.domain.order.AdminOrderQueryRepository;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:config.properties")
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AdminOrderService {

    @Value("${google.api.key}")
    private String apiKey;

    private final AdminOrderQueryRepository adminOrderQueryRepository;

    // 홀 상태 주문 목록 조회
    public Page<HallOrderResponseDto> findHallOrder(String adminLoginId, String status, Pageable pageable) {
        log.info("홀 상태 주문 목록 조회 서비스 {}", adminLoginId);
        Page<HallOrder> hallOrders = adminOrderQueryRepository.findHallOrders(adminLoginId, status, pageable);

        return hallOrders.map(HallOrderResponseDto::new);
    }

    // 배달 상태에 따른 목록 조회
    public Page<DeliveryOrderResponseDto> findDeliveryOrder(String adminLoginId, String status, Pageable pageable) {
        log.info("배달 대기 주문 조회 서비스 {}", adminLoginId);
        Page<DeliveryOrder> deliveryOrders = adminOrderQueryRepository.findDeliveryOrders(adminLoginId, status,
            pageable);

        return deliveryOrders.map(DeliveryOrderResponseDto::new);
    }

    // 월 매출 확인
    public List<CountMonthlyResponseDto> countMonthly(String adminLoginId) {
        log.info("월 매출 확인 서비스 {}", adminLoginId);

        return adminOrderQueryRepository.countMonthly(adminLoginId);
    }

    // 식사 유형에 따른 매출액 비중
    public List<MealTypeResponseDto> mealType(String adminLoginId) {
        log.info("식사 유형에 따른 매출액 비중 서비스 {} ", adminLoginId);
        return adminOrderQueryRepository.mealType(adminLoginId);
    }

    // 각 메뉴별 주문 비중
    public List<MenuPreferResponseDto> menuPrefer(String adminLoginId) {
        log.info("각 메뉴별 주문 비중 {}", adminLoginId);
        return adminOrderQueryRepository.menuPrefer(adminLoginId);
    }

    // 시간대별 매출 발생 추이
    public List<MealTimeResponseDto> mealTime(String adminLoginId) {
        log.info("시간대별 매출 발생 추이 {}", adminLoginId);

        return adminOrderQueryRepository.mealTime(adminLoginId);
    }

    // 지역별 배달 빈도율 반환값 -> 위도, 경도
    public List<Location> deliveryFrequency(String adminLoginId) {
        log.info("지역별 배달 빈도율 {}", adminLoginId);
        List<String> addresses = adminOrderQueryRepository.deliveryFrequency(adminLoginId);
        // 위도, 경도 좌표들
        return getLocations(addresses);
    }

    // 배달주소 -> 위도, 경도 반환
    private List<Location> getLocations(List<String> addresses) {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(apiKey)
            .build();

        List<Location> locationList = new ArrayList<>();

        for (int i = 0; i < addresses.size(); i++) {
            try {
                GeocodingResult[] results = GeocodingApi.geocode(context,
                    addresses.get(i)).await();
                if (results.length > 0) {
                    double latitude = results[0].geometry.location.lat;
                    double longitude = results[0].geometry.location.lng;
                    log.info("위치 정보를 가져왔습니다. 위도: {}, 경도: {}", latitude, longitude);
                    locationList.add(new Location(latitude, longitude));
                } else {
                    locationList.add(new Location(null, null));
                }
            } catch (ApiException | InterruptedException | IOException e) {
                locationList.add(new Location(null, null));
            }
        }
        return locationList;
    }
}
