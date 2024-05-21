package com.ourpos.api.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.request.OrderDetailRequestDto;
import com.ourpos.api.order.request.OrderRequestDto;
import com.ourpos.api.order.response.OrderResponseDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.order.Order;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void createHallOrder(OrderRequestDto orderRequestDto) {
        Order hallOrder = createOrder(orderRequestDto);
        orderRepository.save(hallOrder);
    }

    private Order createOrder(OrderRequestDto orderRequestDto) {
        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId()).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
        Store store = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        List<OrderDetailRequestDto> orderDetailDtos = orderRequestDto.getOrderDetails();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequestDto orderDetailDto : orderDetailDtos) {
            Menu menu = menuRepository.findById(orderDetailDto.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
            orderDetails.add(orderDetailDto.toEntity(menu));
        }

        return orderRequestDto.toEntity(customer, store, orderDetails);
    }

    @Transactional
    @Override
    public void createDeliveryOrder(OrderRequestDto orderRequestDto) {

    }

    @Override
    public OrderResponseDto findOne(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponseDto> findAll() {
        return List.of();
    }
}
