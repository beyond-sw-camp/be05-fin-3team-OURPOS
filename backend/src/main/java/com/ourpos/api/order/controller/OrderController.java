package com.ourpos.api.order.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ourpos.domain.order.TempOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/hall")
    public Result<Void> createHallOrder(@RequestBody HallOrderRequestDto hallOrderRequestDto) {
        log.info("홀 주문: {}", hallOrderRequestDto);
        String loginId = getCustomerLoginId();
        orderService.createHallOrder(loginId, hallOrderRequestDto);

        return new Result<>(HttpStatus.OK.value(), "홀 주문이 완료되었습니다.", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/delivery")
    public Result<Void> createDeliveryOrder(@RequestBody DeliveryOrderRequestDto deliveryOrderRequestDto) {
        log.info("배달 주문: {}", deliveryOrderRequestDto);
        String loginId = getCustomerLoginId();
        orderService.createDeliveryOrder(loginId, deliveryOrderRequestDto, deliveryOrderRequestDto.getTempOrderId());

        return new Result<>(HttpStatus.OK.value(), "배달 주문이 완료되었습니다.", null);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/temp-order")
    public Result<TempOrder> saveTempOrder(@RequestBody Map<String, Object> request) {
        String tempOrderId = (String)request.get("orderId");
        int amount = ((Number)request.get("amount")).intValue();

        TempOrder tempOrder = orderService.saveTempOrder(tempOrderId, amount);
        return new Result<>(HttpStatus.OK.value(), "임시 주문이 저장되었습니다.", tempOrder);
    }

    @DeleteMapping("/temp-order")
    public Result<Void> deleteTempOrder(@RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        orderService.deleteTempOrder(orderId);

        return new Result<>(HttpStatus.OK.value(), "임시 주문이 삭제되었습니다.", null);
    }

    private static String getCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomOAuth2Customer)) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        return ((CustomOAuth2Customer)principal).getLoginId();
    }

}
