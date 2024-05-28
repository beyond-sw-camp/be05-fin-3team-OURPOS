package com.ourpos.domain.order;

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
@Table(name = "order_address")
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_address_id")
    private Long id;

    @Column(name = "order_address_receiver_name")
    private String receiverName;

    @Column(name = "order_address_tel_no")
    private String telNo;

    @Column(name = "order_address_base")
    private String addressBase;

    @Column(name = "order_address_detail")
    private String addressDetail;

    @Column(name = "order_address_zipcode")
    private String zipcode;

    @Builder
    private OrderAddress(String receiverName, String telNo, String addressBase, String addressDetail, String zipcode) {
        this.receiverName = receiverName;
        this.telNo = telNo;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
