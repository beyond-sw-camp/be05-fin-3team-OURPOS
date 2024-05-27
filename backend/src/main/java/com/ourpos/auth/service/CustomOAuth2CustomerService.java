package com.ourpos.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.auth.dto.CustomOAuth2Customer;
import com.ourpos.auth.dto.CustomerAddressLoginDto;
import com.ourpos.auth.dto.CustomerLoginDto;
import com.ourpos.auth.dto.KakaoOAuth2Response;
import com.ourpos.auth.dto.NaverOAuth2Response;
import com.ourpos.auth.dto.OAuth2Response;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerAddress;
import com.ourpos.domain.customer.CustomerRepository;
import com.ourpos.domain.customer.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CustomOAuth2CustomerService extends DefaultOAuth2UserService {

    public static final String NAVER = "naver";
    public static final String KAKAO = "kakao";

    private final CustomerRepository customerRepository;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);

        OAuth2Response oAuth2Response;
        if (registrationId.equals(NAVER)) {
            oAuth2Response = new NaverOAuth2Response(oAuth2User.getAttributes());
        } else if (registrationId.equals(KAKAO)) {
            oAuth2Response = new KakaoOAuth2Response(oAuth2User.getAttributes());
        } else {
            return null;
        }
        log.info("OAuth2Response: {}", oAuth2Response);

        String loginId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        Customer customer = customerRepository.findByLoginId(loginId).orElse(null);
        if (customer == null) {
            log.info("New User");
            customer = Customer.builder()
                .loginId(loginId)
                .name(oAuth2Response.getName())
                .nickname(oAuth2Response.getNickname())
                .role(Role.ROLE_USER)
                .phone(oAuth2Response.getPhone())
                .gender(oAuth2Response.getGender())
                .ageRange(oAuth2Response.getAge())
                .build();
            customerRepository.save(customer);

            CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
                .loginId(loginId)
                .name(oAuth2Response.getName())
                .nickname(oAuth2Response.getNickname())
                .phone(oAuth2Response.getPhone())
                .role(Role.ROLE_USER)
                .build();

            String accessToken = getAccessToken(userRequest);
            if (registrationId.equals(KAKAO)) {
                saveKakaoAddress(accessToken, customer);
                // kakaoService.sendKakaoMessage(accessToken, LocalDateTime.now() + "로그인 시도");
            }
            if (registrationId.equals(NAVER)) {
                saveNaverAddress(accessToken, customer);
            }

            return new CustomOAuth2Customer(customerLoginDto);
        } else {
            log.info("Existing User");
            customer.update(oAuth2Response.getName(), oAuth2Response.getNickname(), oAuth2Response.getPhone());

            CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
                .name(oAuth2Response.getName())
                .loginId(loginId)
                .nickname(oAuth2Response.getNickname())
                .phone(oAuth2Response.getPhone())
                .role(customer.getRole())
                .build();

            if (registrationId.equals(KAKAO)) {
                // String accessToken = getAccessToken(userRequest);
                // kakaoService.sendKakaoMessage(accessToken, LocalDateTime.now() + "로그인 시도");
            }

            return new CustomOAuth2Customer(customerLoginDto);
        }

    }

    private void saveNaverAddress(String accessToken, Customer customer) {
        CustomerAddressLoginDto customerAddressLoginDto = naverService.getPayAddress(accessToken).orElse(null);
        if (customerAddressLoginDto == null) {
            return;
        }
        CustomerAddress customerAddress = CustomerAddress.builder()
            .customer(customer)
            .name(customerAddressLoginDto.getName())
            .receiverName(customerAddressLoginDto.getReceiverName())
            .telNo(customerAddressLoginDto.getReceiverTelNo())
            .addressBase(customerAddressLoginDto.getBaseAddress())
            .addressDetail(customerAddressLoginDto.getDetailAddress())
            .zipcode(customerAddressLoginDto.getZipcode())
            .defaultYn(true)
            .build();

        customer.addAddress(customerAddress);
    }

    private void saveKakaoAddress(String accessToken, Customer customer) {
        CustomerAddressLoginDto customerAddressLoginDto = kakaoService.getKakaoAddress(accessToken);
        CustomerAddress customerAddress = CustomerAddress.builder()
            .customer(customer)
            .name(customerAddressLoginDto.getName())
            .receiverName(customerAddressLoginDto.getReceiverName())
            .telNo(customerAddressLoginDto.getReceiverTelNo())
            .addressBase(customerAddressLoginDto.getBaseAddress())
            .addressDetail(customerAddressLoginDto.getDetailAddress())
            .zipcode(customerAddressLoginDto.getZipcode())
            .defaultYn(true)
            .build();

        customer.addAddress(customerAddress);
    }

    private String getAccessToken(OAuth2UserRequest userRequest) {
        return userRequest.getAccessToken().getTokenValue();
    }
}
