package com.ourpos.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_address_id")
    private Long id;

    @Setter
    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "customer_address_name")
    private String name;

    @Column(name = "customer_address_si")
    private String addressSi;

    @Column(name = "customer_address_gu")
    private String addressGu;

    @Column(name = "customer_address_street_name")
    private String streetName;

    @Column(name = "customer_address_detail")
    private String addressDetail;

    @Column(name = "customer_address_default_yn")
    private Boolean defaultYn;

    @Builder
    private CustomerAddress(String name, String addressSi, String addressGu, String streetName, String addressDetail,
        Boolean defaultYn) {
        this.name = name;
        this.addressSi = addressSi;
        this.addressGu = addressGu;
        this.streetName = streetName;
        this.addressDetail = addressDetail;
        this.defaultYn = defaultYn;
    }
}
