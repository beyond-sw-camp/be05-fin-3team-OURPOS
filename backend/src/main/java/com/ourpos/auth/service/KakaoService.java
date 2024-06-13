package com.ourpos.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ourpos.auth.dto.customer.CustomerAddressLoginDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
@Transactional
@Service
public class KakaoService {

    private final RestTemplate restTemplate;

    public CustomerAddressLoginDto getKakaoAddress(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/shipping_address";
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        List<Map<String, Object>> shippingAddresses = (List<Map<String, Object>>)response.getBody()
            .get("shipping_addresses");

        return getCustomerAddressLogin(shippingAddresses);
    }

    private static CustomerAddressLoginDto getCustomerAddressLogin(
        List<Map<String, Object>> shippingAddresses) {
        CustomerAddressLoginDto customerAddress = null;
        for (Map<String, Object> shippingAddress : shippingAddresses) {
            if (!isDefault(shippingAddress)) {
                continue;
            }
            customerAddress = CustomerAddressLoginDto.builder()
                .name("집")
                .receiverName((String)shippingAddress.get("receiver_name"))
                .receiverTelNo((String)shippingAddress.get("receiver_phone_number1"))
                .baseAddress((String)shippingAddress.get("base_address"))
                .detailAddress((String)shippingAddress.get("detail_address"))
                .zipcode((String)shippingAddress.get("zone_number"))
                .defaultYn(true)
                .build();
        }
        return customerAddress;
    }

    private static boolean isDefault(Map<String, Object> shippingAddress) {
        boolean isDefault = false;
        Object isDefaultObj = shippingAddress.get("is_default");

        if (isDefaultObj instanceof Boolean) {
            isDefault = (Boolean)isDefaultObj;
        } else if (isDefaultObj instanceof String) {
            isDefault = Boolean.parseBoolean((String)isDefaultObj);
        }
        return isDefault;
    }
}

