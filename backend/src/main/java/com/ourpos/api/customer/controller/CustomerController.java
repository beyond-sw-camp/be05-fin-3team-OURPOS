package com.ourpos.api.customer.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerAddressResponseDto;
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

        CustomerResponseDto customerResponse = customerServiceImpl.findLoginCustomer(loginId);
        return new Result<>(HttpStatus.OK.value(), "개인 정보 조회가 완료되었습니다.", customerResponse);
    }

    // 마이페이지- 나의 주소 조회
    @GetMapping("/my/addresses")
    public Result<List<CustomerAddressResponseDto>> findCustomerAddress() {
        String loginId = getLoginCustomerLoginId();
        log.info("나의 주소 조회: {}", loginId);

        List<CustomerAddressResponseDto> customerAddresses = customerServiceImpl.findLoginCustomerAddresses(loginId);
        return new Result<>(HttpStatus.OK.value(), "나의 주소 조회가 완료되었습니다.", customerAddresses);
    }

    // 마이페이지-개인정보 변경(서브주소 추가)
    @PostMapping("/addresses")
    public Result<Void> addAddress(@Valid @RequestBody CustomerAddressRequestDto customerAddressRequestDto) {
        String loginId = getLoginCustomerLoginId();
        log.info("서브주소 추가: {}", loginId);

        customerServiceImpl.addSubAddress(loginId, customerAddressRequestDto);
        return new Result<>(HttpStatus.OK.value(), "서브주소 추가가 완료되었습니다.", null);
    }

    // 마이페이지-개인정보 변경(주소 변경)
    @PutMapping("/addresses/{addressId}")
    public Result<Void> updateAddress(@Valid @PathVariable Long addressId,
        @RequestBody CustomerAddressUpdateDto addressDto) {
        log.info("주소 변경: {}", addressId);

        customerServiceImpl.updateAddress(addressId, addressDto);
        return new Result<>(HttpStatus.OK.value(), "주소 변경이 완료되었습니다.", null);
    }

    // 마이페이지-개인정보 변경(기본 주소 변경)
    @PutMapping("/addresses/{addressId}/default")
    public Result<Void> updateDefaultAddress(@PathVariable Long addressId) {
        String loginId = getLoginCustomerLoginId();
        customerServiceImpl.updateDefaultAddress(loginId, addressId);
        log.info("기본 주소 변경: {}", loginId);

        return new Result<>(HttpStatus.OK.value(), "기본 주소 변경이 완료되었습니다.", null);
    }

    // 마이페이지-개인정보 변경(서브주소 삭제)
    @DeleteMapping("/addresses/{addressId}")
    public Result<Void> deleteAddress(@PathVariable Long addressId) {
        customerServiceImpl.deleteAddress(addressId);
        log.info("서브주소 삭제: {}", addressId);

        return new Result<>(HttpStatus.OK.value(), "서브주소 삭제가 완료되었습니다.", null);
    }

    // 마이페이지 - 내가 주문한 홀/포장 주문 내역 조회
    @GetMapping("/my/orders/hall")
    public Result<Page<HallOrderResponseDto>> getMyOrders(@PageableDefault(size = 10) Pageable pageable) {
        String loginId = getLoginCustomerLoginId();
        log.info("나의 주문 내역 조회: {}", loginId);

        Page<HallOrderResponseDto> hallOrders = orderQueryService.findHallOrderByLoginId(loginId, pageable);
        return new Result<>(HttpStatus.OK.value(), "나의 주문 내역 조회가 완료되었습니다.", hallOrders);
    }

    // 마이페이지 - 내가 주문한 배달 주문 내역 조회
    @GetMapping("/my/orders/delivery")
    public Result<Page<DeliveryOrderResponseDto>> getMyDeliveryOrders(@PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) String status) {
        String loginId = getLoginCustomerLoginId();
        log.info("나의 배달 주문 내역 조회: {}", loginId);

        Page<DeliveryOrderResponseDto> deliveryOrders = orderQueryService.findDeliveryOrderByLoginId(loginId, status,
            pageable);
        return new Result<>(HttpStatus.OK.value(), "나의 배달 주문 내역 조회가 완료되었습니다.", deliveryOrders);
    }

    private String getLoginCustomerLoginId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2Customer customOAuth2Customer = (CustomOAuth2Customer)principal;

        return customOAuth2Customer.getLoginId();
    }

}
