package com.ourpos.api.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.order.dto.request.DeliveryOrderRequestDto;
import com.ourpos.api.order.dto.request.HallOrderRequestDto;
import com.ourpos.api.order.service.OrderService;
import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.exception.LoginRequiredException;

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
        String loginId = getCustomerLoginId();
        orderService.createHallOrder(loginId, hallOrderRequestDto);

        return new Result<>(HttpStatus.OK.value(), "홀 주문이 완료되었습니다.", null);
    }

    @PostMapping("/delivery")
    public Result<Void> createDeliveryOrder(@RequestBody DeliveryOrderRequestDto deliveryOrderRequestDto) {
        log.info("배달 주문: {}", deliveryOrderRequestDto);
        String loginId = getCustomerLoginId();
        orderService.createDeliveryOrder(loginId, deliveryOrderRequestDto);

        return new Result<>(HttpStatus.OK.value(), "배달 주문이 완료되었습니다.", null);
    }

    private static String getCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomOAuth2Customer)) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        return ((CustomOAuth2Customer)principal).getLoginId();
    }

}
