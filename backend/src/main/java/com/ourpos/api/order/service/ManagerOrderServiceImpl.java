package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
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
    public List<HallOrderResponseDto> findHallOrder(Long storeId, String status) {
        log.info("홀 상태 주문 목록 조회 서비스", storeId, status);
        List<HallOrder> hallOrders = orderQueryRepository.findHallOrder(storeId, status);

        return hallOrders.stream()
            .map(HallOrderResponseDto::new)
            .collect(Collectors.toList());
    }

    // 배달 대기 주문 목록 조회
    @Override
    public List<DeliveryOrderResponseDto> checkWaitingDeliverOrder(Long storeId) {
        log.info("배달 대기 주문 조회 서비스 : ", storeId);

        List<DeliveryOrder> deliveryOrders = orderQueryRepository.findAllDeliveryWaiting(storeId);
        List<DeliveryOrderResponseDto> deliveryOrderResponseDtos = new ArrayList<>();

        for (DeliveryOrder order : deliveryOrders) {
            deliveryOrderResponseDtos.add(new DeliveryOrderResponseDto(order));
        }

        return deliveryOrderResponseDtos;
    }

    // 배달 대기 주문 조회

}
