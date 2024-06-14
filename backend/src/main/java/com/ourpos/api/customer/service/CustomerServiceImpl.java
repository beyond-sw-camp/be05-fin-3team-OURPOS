package com.ourpos.api.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.customer.dto.request.CustomerAddressRequestDto;
import com.ourpos.api.customer.dto.request.CustomerAddressUpdateDto;
import com.ourpos.api.customer.dto.response.CustomerAddressResponseDto;
import com.ourpos.api.customer.dto.response.CustomerResponseDto;
import com.ourpos.auth.exception.LoginRequiredException;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerAddressRepository;
import com.ourpos.domain.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;

    //개인 정보 조회
    public CustomerResponseDto findLoginCustomer(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new LoginRequiredException("Customer not found. Id: " + loginId));

        return new CustomerResponseDto(customer);
    }

    //로그인 고객의 주소 조회
    public List<CustomerAddressResponseDto> findLoginCustomerAddresses(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new LoginRequiredException("Customer not found. Id: " + loginId));

        List<CustomerAddress> customerAddresses = customerAddressRepository.findByCustomer(customer);

        return customerAddresses.stream()
            .map(CustomerAddressResponseDto::new)
            .toList();
    }

    //개인정보 변경(서브주소 추가)
    @Transactional
    public void addSubAddress(String loginId, CustomerAddressRequestDto customerAddressRequestDto) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(
            () -> new LoginRequiredException("Customer not found. loginId: " + loginId));

        CustomerAddress customerAddress = createCustomerAddress(customerAddressRequestDto);
        customer.addAddress(customerAddress);
    }

    //개인정보 변경(주소 변경)
    @Transactional
    public void updateAddress(Long addressId, CustomerAddressUpdateDto addressUpdateDto) {
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId).
            orElseThrow(() -> new IllegalArgumentException("Address not found. Id: " + addressId));

        customerAddress.update(addressUpdateDto.getName(), addressUpdateDto.getReceiverName(),
            addressUpdateDto.getTelNo(), addressUpdateDto.getAddressBase(), addressUpdateDto.getAddressDetail(),
            addressUpdateDto.getZipcode());
    }

    //개인정보 변경(서브주소 삭제)
    @Transactional
    public void deleteAddress(Long addressId) {
        // 주소 찾아서 삭제하는 로직
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("Address not found. Id: " + addressId));

        if (Boolean.TRUE.equals(customerAddress.getDefaultYn())) {
            throw new IllegalStateException("기본 주소는 삭제할 수 없습니다.");
        }

        // 주소 삭제
        customerAddress.setCustomer(null);
        customerAddressRepository.delete(customerAddress);
    }

    @Transactional
    public void updateDefaultAddress(String loginId, Long addressId) {
        Customer customer = customerRepository.findByLoginId(loginId)
            .orElseThrow(() -> new LoginRequiredException("Customer not found. loginId: " + loginId));

        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("Address not found. Id: " + addressId));

        customer.updateDefaultAddress(customerAddress);
    }

    private CustomerAddress createCustomerAddress(CustomerAddressRequestDto customerAddressRequestDto) {

        return CustomerAddress.builder()
            .name(customerAddressRequestDto.getName())
            .receiverName(customerAddressRequestDto.getReceiverName())
            .telNo(customerAddressRequestDto.getTelNo())
            .addressBase(customerAddressRequestDto.getAddressBase())
            .addressDetail(customerAddressRequestDto.getAddressDetail())
            .zipcode(customerAddressRequestDto.getZipcode())
            .build();
    }
}

