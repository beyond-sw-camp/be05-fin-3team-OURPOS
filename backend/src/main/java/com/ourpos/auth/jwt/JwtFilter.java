package com.ourpos.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourpos.auth.dto.CustomOAuth2Customer;
import com.ourpos.auth.dto.CustomerLoginDto;
import com.ourpos.domain.customer.Role;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;

        Cookie[] cookies = request.getCookies();
        cookies = cookies == null ? new Cookie[0] : cookies;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
                break;
            }
        }

        if (authorization == null) {
            log.info("Authorization is null");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            log.info("Token is expired");

            filterChain.doFilter(request, response);
            return;
        }

        String loginId = jwtUtil.getLoginId(token);
        Role role = Role.valueOf(jwtUtil.getRole(token));

        CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
            .loginId(loginId)
            .role(role)
            .build();

        CustomOAuth2Customer customOAuth2Customer = new CustomOAuth2Customer(customerLoginDto, false);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            customOAuth2Customer, null, customOAuth2Customer.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
