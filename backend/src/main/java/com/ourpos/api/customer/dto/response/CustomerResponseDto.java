package com.ourpos.api.customer.dto.response;

import java.util.List;

import com.ourpos.domain.customer.Customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDto {

    private Long customerId;
    private String loginId;
    private String name;
    private String nickname;
    private String phone;
    private String gender;
    private String ageRange;
    private String profileImage;
    private List<CustomerAddressResponseDto> customerAddresses;

    public CustomerResponseDto(Customer customer) {
        this.customerId = customer.getId();
        this.loginId = customer.getLoginId();
        this.name = customer.getName();
        this.nickname = customer.getNickname();
        this.phone = customer.getPhone();
        this.gender = customer.getGender();
        this.ageRange = customer.getAgeRange();
        this.profileImage = customer.getProfileImage();
        this.customerAddresses = customer.getCustomerAddresses().stream()
            .map(CustomerAddressResponseDto::new)
            .toList();
    }

}
