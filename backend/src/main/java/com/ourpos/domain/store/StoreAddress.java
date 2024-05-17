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

    @Column(name = "store_address_si")
    private String addressSi;

    @Column(name = "store_address_gu")
    private String addressGu;

    @Column(name = "store_address_street_name")
    private String addressStreetName;

    @Column(name = "store_address_detail")
    private String addressDetail;

    @Builder
    private StoreAddress(String addressSi, String addressGu, String addressStreetName, String addressDetail) {
        this.addressSi = addressSi;
        this.addressGu = addressGu;
        this.addressStreetName = addressStreetName;
        this.addressDetail = addressDetail;
    }
}
