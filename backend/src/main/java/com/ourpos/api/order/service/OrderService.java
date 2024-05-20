package com.ourpos.api.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.order.request.OrderRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.Order;
import com.ourpos.domain.order.OrderRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createHallOrder(OrderRequestDto orderRequestDto) {
        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId()).orElseThrow(
            () -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
        Store store = storeRepository.findById(1L).orElseThrow(
            () -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        Order hallOrder = HallOrder.builder()
            .customer(customer)
            .store(store)
            .orderTakeoutYn(orderRequestDto.getOrderTakeoutYn())
            .build();

        orderRepository.save(hallOrder);
    }
}
