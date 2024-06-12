package com.ourpos.domain.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.ourpos.domain.customer.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    @Column(name = "manager_login_id", unique = true)
    private String loginId;

    @Column(name = "manager_password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Manager(String loginId, String password, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }
}
