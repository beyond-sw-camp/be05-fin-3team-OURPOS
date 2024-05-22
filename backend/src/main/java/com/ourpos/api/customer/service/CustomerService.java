package com.ourpos.api.customer.service;

import org.springframework.stereotype.Service;

import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.request.CustomerRequestDto;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;


@Service
public interface CustomerService {

	void getCustomer(Long customerId);

	void updateAddress(Long customerId, CustomerAddressUpdateDto addressupdateDto);

	void addSubAddress(Long customerId, CustomerRequestDto addressDto);

	void deleteAddress(Long addressId);




}
