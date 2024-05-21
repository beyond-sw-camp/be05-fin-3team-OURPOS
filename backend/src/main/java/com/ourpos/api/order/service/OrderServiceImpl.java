package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;
import com.ourpos.api.order.dto.request.OrderDetailRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    public static final String ORDER_NOT_FOUND = "해당 주문이 존재하지 않습니다.";

    private final OrderRepository<HallOrder> hallOrderRepository;
    private final OrderRepository<DeliveryOrder> deliveryOrderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Override
    public void createHallOrder(HallOrderRequestDto hallOrderRequestDto) {
        HallOrder hallOrder = createOrder(hallOrderRequestDto);
        hallOrderRepository.save(hallOrder);
    }

    private HallOrder createOrder(HallOrderRequestDto hallOrderRequestDto) {
        Customer customer = customerRepository.findById(hallOrderRequestDto.getCustomerId()).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
        Store store = storeRepository.findById(hallOrderRequestDto.getStoreId()).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        List<OrderDetailRequestDto> orderDetailDtos = hallOrderRequestDto.getOrderDetails();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }

        return hallOrderRequestDto.toEntity(customer, store, orderDetails);
    }

    @Override
    public void createDeliveryOrder(DeliveryOrderRequestDto deliveryOrderRequestDto) {
        DeliveryOrder deliveryOrder = createOrder(deliveryOrderRequestDto);
        deliveryOrderRepository.save(deliveryOrder);
    }

    private DeliveryOrder createOrder(DeliveryOrderRequestDto deliveryOrderRequestDto) {
        Customer customer = customerRepository.findById(deliveryOrderRequestDto.getCustomerId()).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
        Store store = storeRepository.findById(deliveryOrderRequestDto.getStoreId()).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        List<OrderDetailRequestDto> orderDetailDtos = deliveryOrderRequestDto.getOrderDetails();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }
        return deliveryOrderRequestDto.toEntity(customer, store, orderDetails);
    }

    @Override
    public void cancelHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.cancelOrder();
    }

    @Override
    public void acceptHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.acceptOrder();
    }

    @Override
    public void completeHallOrder(Long orderId) {
        HallOrder order = hallOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.completeOrder();
    }

    @Override
    public void cancelDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.cancelOrder();
    }

    @Override
    public void acceptDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.acceptOrder();
    }

    @Override
    public void startDelivery(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.startDelivery();
    }

    @Override
    public void completeDeliveryOrder(Long orderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND));

        order.completeOrder();
    }
}
