package com.ourpos.auth.dto;

import com.ourpos.domain.customer.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerLoginDto {

    private String loginId;
    private String name;
    private String nickname;
    private String phone;
    private Role role;

    @Builder
    public CustomerLoginDto(String loginId, String name, String nickname, String phone, Role role) {
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.role = role;
    }
}
