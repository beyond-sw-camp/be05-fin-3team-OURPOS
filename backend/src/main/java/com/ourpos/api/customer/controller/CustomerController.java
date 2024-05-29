package com.ourpos.api.customer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.api.customer.service.CustomerServiceImpl;
import com.ourpos.api.order.dto.response.DeliveryOrderResponseDto;
import com.ourpos.api.order.dto.response.HallOrderResponseDto;
import com.ourpos.api.order.service.OrderQueryService;
import com.ourpos.auth.dto.CustomOAuth2Customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerServiceImpl customerServiceImpl;
    private final OrderQueryService orderQueryService;

    // 마이페이지- 개인정보조회
    @GetMapping("/my")
    public Result<CustomerResponseDto> findCustomer() {
        String loginId = getLoginCustomerLoginId();
        log.info("개인 정보 조회: {}", loginId);

        return new Result<>(HttpStatus.OK.value(), "개인 정보 조회가 완료되었습니다.", customerServiceImpl.findCustomer(loginId));
    }

    //마이페이지-개인정보 변경(주소 변경)
    @PutMapping("/address/{addressId}")
    public Result<Void> updateAddress(@PathVariable Long addressId, @RequestBody CustomerAddressUpdateDto addressDto) {
        customerServiceImpl.updateAddress(addressId, addressDto);

        return new Result<>(HttpStatus.OK.value(), "주소 변경이 완료되었습니다.", null);
    }

    //마이페이지-개인정보 변경(서브주소 추가)
    @PostMapping("/address")
    public Result<Void> addAddress(@RequestBody CustomerAddressRequestDto customerAddressRequestDto) {
        String loginId = getLoginCustomerLoginId();

        customerServiceImpl.addSubAddress(loginId, customerAddressRequestDto);
        return new Result<>(HttpStatus.OK.value(), "서브주소 추가가 완료되었습니다.", null);
    }

    //마이페이지-개인정보 변경(서브주소 삭제)
    @DeleteMapping("/address/{addressId}")
    public Result<Void> deleteAddress(@PathVariable Long addressId) {
        customerServiceImpl.deleteAddress(addressId);

        return new Result<>(HttpStatus.OK.value(), "서브주소 삭제가 완료되었습니다.", null);
    }

    @GetMapping("/my/orders/hall")
    public Result<List<HallOrderResponseDto>> getMyOrders() {
        String loginId = getLoginCustomerLoginId();
        log.info("나의 주문 내역 조회: {}", loginId);

        List<HallOrderResponseDto> hallOrders = orderQueryService.findHallOrderByLoginId(loginId);
        return new Result<>(HttpStatus.OK.value(), "나의 주문 내역 조회가 완료되었습니다.", hallOrders);
    }

    @GetMapping("/my/orders/delivery")
    public Result<List<DeliveryOrderResponseDto>> getMyDeliveryOrders() {
        String loginId = getLoginCustomerLoginId();
        log.info("나의 배달 주문 내역 조회: {}", loginId);

        List<DeliveryOrderResponseDto> deliveryOrders = orderQueryService.findDeliveryOrderByLoginId(loginId);
        return new Result<>(HttpStatus.OK.value(), "나의 배달 주문 내역 조회가 완료되었습니다.", deliveryOrders);
    }

    private static String getLoginCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2Customer customOAuth2Customer = (CustomOAuth2Customer)principal;

        return customOAuth2Customer.getLoginId();
    }

}
