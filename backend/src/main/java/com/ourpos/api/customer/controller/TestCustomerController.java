package com.ourpos.api.customer.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;
import com.ourpos.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TestCustomerController {

    private final JwtUtil jwtUtil;

    @PostMapping("/test/login")
    public Result<Void> testLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = jwtUtil.createJwt("test", "ROLE_USER", 1000 * 60L * 60 * 24 * 7);
        ResponseCookie cookie = createCookie(token);

        response.addHeader("Set-Cookie", cookie.toString());
        return new Result<>(HttpStatus.OK.value(), "테스트 로그인 성공", null);
    }

    private ResponseCookie createCookie(String value) {

        return ResponseCookie.from("Authorization", value)
            .httpOnly(true)
            .maxAge(1000 * 60 * 60 * 24 * 7)
            .domain(".ourpos.org")
            .path("/")
            .sameSite("None")
            .secure(true)
            .build();
    }
}