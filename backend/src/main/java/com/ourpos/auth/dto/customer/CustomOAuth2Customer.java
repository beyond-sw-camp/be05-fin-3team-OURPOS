package com.ourpos.auth.dto.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2Customer implements OAuth2User {

    private final CustomerLoginDto customerLoginDto;
    private final boolean isNewUser;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> customerLoginDto.getRole().name());
        return authorities;
    }

    @Override
    public String getName() {
        return customerLoginDto.getName();
    }

    public String getLoginId() {
        return customerLoginDto.getLoginId();
    }

    public boolean isNewUser() {
        return isNewUser;
    }

}