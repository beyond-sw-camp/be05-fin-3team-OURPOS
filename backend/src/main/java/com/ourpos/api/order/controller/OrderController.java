package com.ourpos.api.order.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.order.request.OrderRequestDto;
import com.ourpos.api.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/hall")
    public void createHallOrder(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("홀 주문: {}", orderRequestDto);
        orderService.createHallOrder(orderRequestDto);
    }

}
