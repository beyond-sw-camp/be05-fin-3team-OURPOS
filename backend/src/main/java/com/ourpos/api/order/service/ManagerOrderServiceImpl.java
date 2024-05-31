package com.ourpos.api.order.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:config.properties")
@Service
@RequiredArgsConstructor
public class ManagerOrderServiceImpl implements ManagerOrderService {

    @Value("${google.api.key}")
    private String apiKey;

    private final OrderQueryRepository orderQueryRepository;

    // 홀 상태 주문 목록 조회
    @Override
    public Page<HallOrderResponseDto> findHallOrder(Long storeId, String status, Pageable pageable) {
        log.info("홀 상태 주문 목록 조회 서비스 {} {}", storeId, status);
        Page<HallOrder> hallOrders = orderQueryRepository.findHallOrder(storeId, status, pageable);

        return hallOrders.map(HallOrderResponseDto::new);
    }

    // 배달 상태에 따른 목록 조회
    @Override
    public Page<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status, Pageable pageable) {
        log.info("배달 대기 주문 조회 서비스 {} {} ", storeId, status);
        Page<DeliveryOrder> deliveryOrders = orderQueryRepository.findDeliveryOrder(storeId, status, pageable);

        return deliveryOrders.map(DeliveryOrderResponseDto::new);
    }

    // 월 매출 확인
    @Override
    public List<CountMonthlyResponseDto> countMonthly(Long storeId) {
        log.info("월 매출 확인 서비스 {}", storeId);

        List<CountMonthlyResponseDto> dtos = orderQueryRepository.countMonthly(storeId);

        return dtos;
    }

    // 식사 유형에 따른 매출액 비중
    @Override
    public List<MealTypeResponseDto> mealType(Long storeId) {
        log.info("식사 유형에 따른 매출액 비중 서비스 {} ", storeId);

        List<MealTypeResponseDto> dtos = orderQueryRepository.mealType(storeId);
        return dtos;
    }

    // 각 메뉴별 주문 비중
    @Override
    public List<MenuPreferResponseDto> menuPrefer(Long storeId) {
        log.info("각 메뉴별 주문 비중 {}", storeId);
        return orderQueryRepository.menuPrefer(storeId);
    }

    // 시간대별 매출 발생 추이
    @Override
    public List<MealTimeResponseDto> mealTime(Long storeId) {
        log.info("시간대별 매출 발생 추이 {}", storeId);
        List<MealTimeResponseDto> dtos = orderQueryRepository.mealTime(storeId);

        return dtos;
    }

    // 지역별 배달 빈도율 반환값 -> 위도, 경도
    @Override
    public List<Location> deliveryFrequency(Long storeId) {
        log.info("지역별 배달 빈도율 {}", storeId);
        List<String> addresses = orderQueryRepository.deliveryFrequency(storeId);
        // 위도, 경도 좌표들
        List<Location> locations = getLocations(addresses);
        return locations;
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
