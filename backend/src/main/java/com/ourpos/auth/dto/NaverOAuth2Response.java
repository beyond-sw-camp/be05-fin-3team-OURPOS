package com.ourpos.auth.dto;

import java.util.Map;

public class NaverOAuth2Response implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverOAuth2Response(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>)attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProfileImage() {
        return attributes.get("profile_image").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getGender() {
        return attributes.get("gender").toString();
    }

    @Override
    public String getAge() {
        return attributes.get("age").toString();
    }

    @Override
    public String getPhone() {
        return attributes.get("mobile").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

}
