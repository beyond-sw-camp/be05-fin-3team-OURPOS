package com.ourpos.auth.dto;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getNickname();

    String getGender();

    String getAge();

    String getPhone();

}
