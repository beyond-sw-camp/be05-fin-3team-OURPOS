package com.ourpos.auth.dto.customer;

import java.util.Map;

public class KakaoOAuth2Response implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoOAuth2Response(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProfileImage() {
        Object properties = attributes.get("properties");
        Map<String, Object> propertiesMap = (Map<String, Object>)properties;
        return propertiesMap.get("thumbnail_image").toString();
    }

    @Override
    public String getName() {
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>)kakaoAccount;
        return kakaoAccountMap.get("name").toString();
    }

    @Override
    public String getNickname() {
        Object properties = attributes.get("properties");
        Map<String, Object> propertiesMap = (Map<String, Object>)properties;
        return propertiesMap.get("nickname").toString();
    }

    @Override
    public String getGender() {
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>)kakaoAccount;
        String gender = kakaoAccountMap.get("gender").toString();
        if (gender.equals("male")) {
            return "M";
        } else {
            return "F";
        }
    }

    @Override
    public String getAge() {
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>)kakaoAccount;
        return kakaoAccountMap.get("age_range").toString().replace("~", "-");
    }

    @Override
    public String getPhone() {
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>)kakaoAccount;
        String phoneNumber = kakaoAccountMap.get("phone_number").toString();
        return phoneNumber.replace("+82", "0").replace("-", "");
    }

    @Override
    public String getEmail() {
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>)kakaoAccount;
        return kakaoAccountMap.get("email").toString();
    }

}
