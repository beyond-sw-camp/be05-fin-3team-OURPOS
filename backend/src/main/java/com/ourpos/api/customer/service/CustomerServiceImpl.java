package com.ourpos.api.customer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.api.order.service.OrderQueryService;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerAddressRepository;
import com.ourpos.domain.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl {
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderQueryService orderQueryService;

    //개인 정보 조회
    public CustomerResponseDto findCustomer(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found. Id: " + loginId));

        return new CustomerResponseDto(customer);
    }

    //주문 내역 조회
    //매장이름, {주문메뉴이름, 주문 수량, 주문 상세 가격}, 총 가격, 주문 날짜
	/*
	public List<OrderResponseDto> findAllByCustomerId(Long customerId) {
		List<Order> orders = orderRepository.findAllByCustomerId(customerId)
			.orElseThrow(() -> new IllegalArgumentException("Order not found. Customer Id: " + customerId));

		return orders.stream()
			.map(OrderResponseDto::new)
			.toList();
	}
	 */

    //개인정보 변경(주소 변경)
    @Transactional
    public void updateAddress(Long addressId, CustomerAddressUpdateDto addressUpdateDto) {
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId).
            orElseThrow(() -> new IllegalArgumentException("Address not found. Id: " + addressId));

        customerAddress.update(addressUpdateDto.getName(), addressUpdateDto.getReceiverName(),
            addressUpdateDto.getTelNo(), addressUpdateDto.getAddressBase(), addressUpdateDto.getAddressDetail(),
            addressUpdateDto.getZipcode());
    }

    //개인정보 변경(서브주소 추가)
    @Transactional
    public void addSubAddress(String loginId, CustomerAddressRequestDto customerAddressRequestDto) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(
            () -> new IllegalArgumentException("Customer not found. loginId: " + loginId));

        CustomerAddress customerAddress = CustomerAddress.builder()
            .customer(customer)
            .name(customerAddressRequestDto.getName())
            .receiverName(customerAddressRequestDto.getReceiverName())
            .telNo(customerAddressRequestDto.getTelNo())
            .addressBase(customerAddressRequestDto.getAddressBase())
            .addressDetail(customerAddressRequestDto.getAddressDetail())
            .zipcode(customerAddressRequestDto.getZipcode())
            .build();

        customer.addAddress(customerAddress);
        customerRepository.save(customer);
    }

    //개인정보 변경(서브주소 삭제)
    @Transactional
    public void deleteAddress(Long addressId) {
        // 주소 찾아서 삭제하는 로직
        CustomerAddress address = customerAddressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("Address not found. Id: " + addressId));

        // 주소 삭제
        if (address.getDefaultYn()) {
            throw new IllegalStateException("기본 주소는 삭제할 수 없습니다.");
        }

        customerAddressRepository.delete(address);
    }

}

