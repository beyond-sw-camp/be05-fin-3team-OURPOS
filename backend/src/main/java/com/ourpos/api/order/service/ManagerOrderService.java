package com.ourpos.api.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.CountMonthlyResponseDto;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.dto.response.MealTypeResponseDto;

@Service
public interface ManagerOrderService {

    // 홀 주문 목록 확인
    List<HallOrderResponseDto> findHallOrder(Long storeId, String status);

    List<DeliveryOrderResponseDto> findDeliveryOrder(Long storeId, String status);

    List<CountMonthlyResponseDto> countMonthly(Long storeId);

    // 식사 유형에 따른 매출액 비중
    List<MealTypeResponseDto> mealType(Long storeId);
}
