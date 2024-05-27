package com.ourpos.api.customer.dto.response;

import java.util.List;

import com.ourpos.domain.customer.Customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDto {

    private Long customerId;
    private String nickname;
    private String password;
    private String phone;
    private List<CustomerAddressResponseDto> customerAddresses;

    public CustomerResponseDto(Customer customer) {
        this.customerId = customer.getId();
        this.nickname = customer.getNickname();
        this.password = customer.getPassword();
        this.phone = customer.getPhone();
        this.customerAddresses = customer.getCustomerAddresses().stream()
            .map(CustomerAddressResponseDto::new)
            .toList();
    }

}
