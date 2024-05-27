package com.ourpos.api.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;
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
    public Result<Void> createHallOrder(@RequestBody HallOrderRequestDto hallOrderRequestDto) {
        log.info("홀 주문: {}", hallOrderRequestDto);
        orderService.createHallOrder(hallOrderRequestDto);

        return new Result<>(HttpStatus.OK.value(), "홀 주문이 완료되었습니다.", null);
    }

    @PostMapping("/delivery")
    public Result<Void> createDeliveryOrder(@RequestBody DeliveryOrderRequestDto deliveryOrderRequestDto) {
        log.info("배달 주문: {}", deliveryOrderRequestDto);
        orderService.createDeliveryOrder(deliveryOrderRequestDto);

        return new Result<>(HttpStatus.OK.value(), "배달 주문이 완료되었습니다.", null);
    }

}
