package com.ourpos.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ourpos.auth.dto.CustomerAddressLoginDto;

@Service
public class KakaoService {

    public void sendKakaoMessage(String accessToken, String message) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("template_object", "{\n" +
            "  \"object_type\": \"text\",\n" +
            "  \"text\": \"" + message + "\",\n" +
            "  \"link\": {\n" +
            "    \"web_url\": \"http://www.localhost:8080\",\n" +
            "    \"mobile_web_url\": \"http://www.localhost:8080\"\n" +
            "  },\n" +
            "  \"button_title\": \"바로 확인\"\n" +
            "}");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
    }

    public CustomerAddressLoginDto getKakaoAddress(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/shipping_address";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        List<Map<String, Object>> shippingAddresses = (List<Map<String, Object>>)response.getBody()
            .get("shipping_addresses");

        CustomerAddressLoginDto customerAddressLogin = getCustomerAddressLogin(shippingAddresses);
        return customerAddressLogin;
    }

    private static CustomerAddressLoginDto getCustomerAddressLogin(
        List<Map<String, Object>> shippingAddresses) {
        CustomerAddressLoginDto customerAddress = null;
        for (Map<String, Object> shippingAddress : shippingAddresses) {
            Boolean isDefault = getIsDefault(shippingAddress);
            if (isDefault == null || !isDefault) {
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

    private static Boolean getIsDefault(Map<String, Object> shippingAddress) {
        Boolean isDefault = false;
        Object isDefaultObj = shippingAddress.get("is_default");

        if (isDefaultObj instanceof Boolean) {
            isDefault = (Boolean)isDefaultObj;
        } else if (isDefaultObj instanceof String) {
            isDefault = Boolean.parseBoolean((String)isDefaultObj);
        }
        return isDefault;
    }
}
