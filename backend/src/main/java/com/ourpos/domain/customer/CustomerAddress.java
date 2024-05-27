package com.ourpos.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "customer_address")
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

    @Column(name = "customer_adrress_receiver_name")
    private String receiverName;

    @Column(name = "customer_address_tel_no")
    private String telNo;

    @Column(name = "customer_address_base")
    private String addressBase;

    @Column(name = "customer_address_detail")
    private String addressDetail;

    @Column(name = "customer_address_zipcode")
    private String zipcode;

    @Column(name = "customer_address_default_yn")
    private Boolean defaultYn;

    @Builder
    public CustomerAddress(Customer customer, String name, String receiverName, String telNo, String addressBase,
        String addressDetail, String zipcode, Boolean defaultYn) {
        this.customer = customer;
        this.name = name;
        this.receiverName = receiverName;
        this.telNo = telNo;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.defaultYn = defaultYn;
    }

    public void update(String name, String receiverName, String telNo, String addressBase, String addressDetail,
        String zipcode) {
        this.name = name;
        this.receiverName = receiverName;
        this.telNo = telNo;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }

    public void updateDefaultYn(Boolean defaultYn) {
        this.defaultYn = defaultYn;
    }
}
