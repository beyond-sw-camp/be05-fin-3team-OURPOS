package com.ourpos.api.customer.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.dto.customer.CustomerLoginDto;
import com.ourpos.auth.jwt.JwtUtil;
import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TestCustomerController {

    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    @PostMapping("/test/login")
    public Result<Void> testLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = jwtUtil.createJwt("test", "ROLE_USER", 1000 * 60L * 60 * 24 * 7);
        ResponseCookie cookie = createCookie(token);

        response.addHeader("Set-Cookie", cookie.toString());

        Customer testCustomer = customerRepository.findByLoginId("test")
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
            .loginId(testCustomer.getLoginId())
            .name(testCustomer.getName())
            .nickname(testCustomer.getNickname())
            .phone(testCustomer.getPhone())
            .role(testCustomer.getRole())
            .build();

        CustomOAuth2Customer customOAuth2Customer = new CustomOAuth2Customer(customerLoginDto, false);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            customOAuth2Customer, null, customOAuth2Customer.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return new Result<>(HttpStatus.OK.value(), "테스트 로그인 성공", null);
    }

    private ResponseCookie createCookie(String value) {

        return ResponseCookie.from("Authorization", value)
            .httpOnly(true)
            .maxAge(60 * 60 * 24 * 7)
            .domain(".ourpos.org")
            .path("/")
            .sameSite("None")
            .secure(true)
            .build();
    }
}
