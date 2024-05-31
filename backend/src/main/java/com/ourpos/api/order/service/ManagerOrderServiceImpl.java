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
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerOrderServiceImpl implements ManagerOrderService {

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
}
