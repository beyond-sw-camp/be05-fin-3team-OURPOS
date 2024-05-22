package com.ourpos.api.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.request.CustomerRequestDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.api.customer.service.CustomerServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerServiceImpl customerServiceImpl;

    // 마이페이지- 개인정보조회
    @GetMapping("/{customerId}")
    public CustomerResponseDto getCustomer(@PathVariable Long customerId) {
        CustomerResponseDto customer = customerServiceImpl.getCustomer(customerId);
        return customer;
    }

    // 마이페이지- 주문내역조회(페이징처리x)
	/*
	@GetMapping("/{customerId}/orders") // 중괄호 추가
	public List<OrderResponseDto> getOrdersByCustomerId(@PathVariable Long customerId) {
		List<OrderResponseDto> orders = CustomerService.findAllByCustomerId(customerId);
		return orders;
	}
	*/

    //마이페이지-개인정보 변경(주소 변경)
    @PutMapping("/address/{addressId}")
    public ResponseEntity<Void> updateAddress(@PathVariable Long addressId,
        @RequestBody CustomerAddressUpdateDto addressDto) {
        customerServiceImpl.updateAddress(addressId, addressDto);
        return ResponseEntity.noContent().build(); // 수정 후 상태 코드 204 (No Content) 반환
    }

    //마이페이지-개인정보 변경(서브주소 추가)
    @PostMapping("/address")
    public ResponseEntity<Void> addAddress(@RequestBody CustomerRequestDto addressDto) {
        Long customerId = addressDto.getCustomerId();
        customerServiceImpl.addSubAddress(customerId, addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //마이페이지-개인정보 변경(서브주소 삭제)
    @DeleteMapping("/address/{addressId}")
    public Void deleteAddress(@PathVariable Long addressId) {
        customerServiceImpl.deleteAddress(addressId);
        return null;
    }

    //마이페이지-비밀번호 변경

}
