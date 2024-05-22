package com.ourpos.domain.customer;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.ourpos.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_login_id")
    private String loginId;

    @Column(name = "customer_password")
    private String password;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "customer_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "customer_nickname")
    private String nickname;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerAddress> customerAddresses = new ArrayList<>();

    @Builder
    private Customer(String loginId, String password, String name, String phone, Role role, String nickname,
        @Singular List<CustomerAddress> customerAddresses) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.nickname = nickname;
        for (CustomerAddress customerAddress : customerAddresses) {
            addCustomerAddress(customerAddress);
        }
    }

    // 연관관계 편의 메서드
    public void addCustomerAddress(CustomerAddress customerAddress) {
        customerAddresses.add(customerAddress);
        customerAddress.setCustomer(this);
    }

    public void update(String addressSi, String addressGu, String streetName, String addressDetail) {
        for (CustomerAddress address : this.customerAddresses) {

            address.setAddressSi(addressSi);
            address.setAddressGu(addressGu);
            address.setStreetName(streetName);
            address.setAddressDetail(addressDetail);
        }
    }

    public void addAddress(CustomerAddress customerAddress) {
        if (customerAddresses.size() < 3) {
            addCustomerAddress(customerAddress);
        } else {
            throw new IllegalStateException("주소는 최대 3개까지 추가 가능합니다.");
        }
    }

    }




