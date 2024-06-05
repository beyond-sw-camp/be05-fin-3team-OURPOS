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
    private String addressBase;

    @Column(name = "store_address_detail")
    private String addressDetail;

    @Column(name = "store_address_zipcode")
    private String zipcode;

    @Column(name = "store_address_latitude")
    private Double latitude;

    @Column(name = "store_address_longitude")
    private Double longitude;

    @Builder
    private StoreAddress(String addressBase, String addressDetail, String zipcode, Double latitude, Double longitude) {
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
