package com.ourpos.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ourpos.auth.dto.customer.CustomerAddressLoginDto;

@Service
public class NaverService {

    public Optional<CustomerAddressLoginDto> getPayAddress(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://openapi.naver.com/v1/nid/payaddress";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
            Map<String, String> address = (Map<String, String>)response.getBody().get("data");
            return Optional.ofNullable(CustomerAddressLoginDto.builder()
                .name("집")
                .receiverName(address.get("receiverName"))
                .receiverTelNo(address.get("telNo"))
                .zipcode(address.get("zipCode"))
                .baseAddress(address.get("baseAddress"))
                .detailAddress(address.get("detailAddress"))
                .build());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
