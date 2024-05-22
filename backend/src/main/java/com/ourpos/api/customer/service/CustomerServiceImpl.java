package com.ourpos.api.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.request.CustomerRequestDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerAddressRepository;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.order.OrderRepository;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl {
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;
	private final CustomerAddressRepository customerAddressRepository;

	//개인 정보 조회
	public CustomerResponseDto getCustomer(Long customerId) {
		Customer customer = customerRepository.findByCustomerId(customerId)
			.orElseThrow(() -> new IllegalArgumentException("Customer not found. Id: " + customerId));

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
	public void updateAddress(Long addressId, CustomerAddressUpdateDto addressupdateDto) {
		Customer customer = customerRepository.findById(addressId).orElseThrow(
			() -> new IllegalArgumentException("Customer not found. Id: " + addressId));

		customer.update(addressupdateDto.getAddressSi(), addressupdateDto.getAddressGu(),
			addressupdateDto.getStreetName(), addressupdateDto.getAddressDetail());

		customerRepository.save(customer);

	}

	//개인정보 변경(서브주소 추가)
	@Transactional
	public void addSubAddress(Long customerId, CustomerRequestDto addressDto) {

		Customer customer = customerRepository.findById(customerId).orElseThrow(
			() -> new IllegalArgumentException("Customer not found. Id: " + customerId));

		CustomerAddress customerAddress = CustomerAddress.builder()
			.name(addressDto.getAddressName())
			.addressSi(addressDto.getAddressSi())
			.addressGu(addressDto.getAddressGu())
			.streetName(addressDto.getStreetName())
			.addressDetail(addressDto.getAddressDetail())
			.defaultYn(addressDto.getDefaultYn())
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
		customerAddressRepository.delete(address);

	}


	}

