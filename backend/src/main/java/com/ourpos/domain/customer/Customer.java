package com.ourpos.domain.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.ourpos.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    private Customer(String loginId, String password, String name, String phone, Role role, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.nickname = nickname;
    }
    
}
