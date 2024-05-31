package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.CountMonthlyResponseDto;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.dto.response.MealTimeResponseDto;
import com.ourpos.api.order.dto.response.MealTypeResponseDto;
import com.ourpos.api.order.dto.response.MenuPreferResponseDto;
import com.ourpos.api.store.Location;

@Service
public interface ManagerOrderService {

    // 홀 주문 목록 확인
    Page<HallOrderResponseDto> findHallOrder(Long storeId, String status, Pageable pageable);

    Page<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status, Pageable pageable);

    List<CountMonthlyResponseDto> countMonthly(Long storeId);

    // 식사 유형에 따른 매출액 비중
    List<MealTypeResponseDto> mealType(Long storeId);

    // 각 메뉴별 주문 비중
    List<MenuPreferResponseDto> menuPrefer(Long storeId);

    // 시간대별 매출 발생 추이
    List<MealTimeResponseDto> mealTime(Long storeId);

    // 지역별 배달 빈도율 반환값 -> 위도, 경도
    List<Location> deliveryFrequency(Long storeId);
}
