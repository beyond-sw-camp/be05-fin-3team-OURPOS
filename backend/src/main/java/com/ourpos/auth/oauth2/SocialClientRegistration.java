package com.ourpos.auth.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config.properties")
@Component
public class SocialClientRegistration {

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;

    public ClientRegistration naverClientRegistration() {

        return ClientRegistration.withRegistrationId("naver")
            .clientId(naverClientId)
            .clientSecret(naverClientSecret)
            .redirectUri("http://localhost:8080/login/oauth2/code/naver")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("name", "nickname", "gender", "age", "mobile", "profile_image")
            .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
            .tokenUri("https://nid.naver.com/oauth2.0/token")
            .userInfoUri("https://openapi.naver.com/v1/nid/me")
            .userNameAttributeName("response")
            .build();
    }

    public ClientRegistration kakaoClientRegistration() {

        return ClientRegistration.withRegistrationId("kakao")
            .clientId(kakaoClientId)
            .clientSecret(kakaoClientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("name", "profile_nickname", "gender", "age_range", "shipping_address", "phone_number",
                "profile_image", "talk_message")
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .tokenUri("https://kauth.kakao.com/oauth/token")
            .userInfoUri("https://kapi.kakao.com/v2/user/me")
            .userNameAttributeName("id")
            .build();
    }
}