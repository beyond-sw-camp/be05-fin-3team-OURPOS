package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;

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

    // 레파지토리에서 모든 정보를 가지고 온 후, 해당 id 를 이용해서, Order order 로, 값을 하나하나 넣고, dto 에 값을 다시 대입

    private final OrderQueryRepository orderQueryRepository;

    // 홀 대기 상태인 주문 목록 조회
    @Override
    public List<HallOrderResponseDto> checkWaitingOrder(Long storeId) {
        System.out.println("ManagerOrderServiceImpl.checkWaitingOrder");
        List<HallOrder> hallOrders = orderQueryRepository.findWaitingAll(storeId);
        List<HallOrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (HallOrder order : hallOrders) {
            orderResponseDtos.add(new HallOrderResponseDto(order));
        }

        return orderResponseDtos;
    }

    // 홀 조리 상태인 주문 목록 조회
    @Override
    public List<HallOrderResponseDto> checkCookingOrder(Long storeId) {
        System.out.println("ManagerOrderServiceImpl.checkCookingOrder");
        List<HallOrder> hallOrders = orderQueryRepository.findCookingAll(storeId);
        List<HallOrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (HallOrder order : hallOrders) {
            orderResponseDtos.add(new HallOrderResponseDto(order));
        }
        return orderResponseDtos;
    }

    // 홀 완료 상태인 주문 목록 조회
    @Override
    public List<HallOrderResponseDto> checkCompleteOrder(Long storeId) {

        System.out.println("ManagerOrderServiceImpl.checkCompleteOrder");
        List<HallOrder> hallOrders = orderQueryRepository.findCompleteAll(storeId);
        List<HallOrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (HallOrder order : hallOrders) {
            orderResponseDtos.add(new HallOrderResponseDto(order));
        }
        return orderResponseDtos;
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
