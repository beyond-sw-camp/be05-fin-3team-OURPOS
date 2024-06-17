package com.ourpos.auth.dto.customer;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getProfileImage();

    String getName();

    String getNickname();

    String getGender();

    String getAge();

    String getPhone();

    String getEmail();
}
