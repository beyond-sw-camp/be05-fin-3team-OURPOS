package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.dto.request.KioskOrderRequestDto;
import com.ourpos.api.order.dto.request.OrderDetailRequestDto;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class KioskOrderService {

    private final OrderRepository<HallOrder> orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public void createKioskOrder(String loginId, KioskOrderRequestDto kioskOrderRequestDto) {
        log.info("키오스크 주문 생성");
        Store store = storeRepository.findByManagerLoginId(loginId).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
        List<OrderDetail> orderDetails = createOrderDetails(kioskOrderRequestDto.getOrderDetailDtos());

        HallOrder order = kioskOrderRequestDto.toEntity(store, orderDetails);
        orderRepository.save(order);
    }

    private List<OrderDetail> createOrderDetails(List<OrderDetailRequestDto> orderDetailRequestDtos) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailRequestDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }
        return orderDetails;
    }
}
