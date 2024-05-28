package com.ourpos.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_address")
public class StoreAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_address_id")
    private Long id;

    @Column(name = "store_address_base")
    private String addressSi;

    @Column(name = "store_address_detail")
    private String addressDetail;

    @Column(name = "store_address_zipcode")
    private String zipcode;

    @Builder
    private StoreAddress(String addressSi, String addressDetail, String zipcode) {
        this.addressSi = addressSi;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
