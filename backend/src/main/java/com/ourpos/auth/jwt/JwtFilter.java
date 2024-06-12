package com.ourpos.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourpos.auth.config.SecurityConfig;
import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.dto.customer.CustomerLoginDto;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.customer.Role;
import com.ourpos.domain.manager.Manager;

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

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String[] superAdminPaths = SecurityConfig.hasRoleSuperAdmin.get(method);
        String[] managerPaths = SecurityConfig.hasRoleAdmin.get(method);
        String[] userPaths = SecurityConfig.hasRoleUser.get(method);

        if (accessManagerPath(request, response, filterChain, managerPaths, requestURI)) {
            return;
        }

        if (accessManagerPath(request, response, filterChain, superAdminPaths, requestURI)) {
            return;
        }

        // if (accessUserPath(request, response, filterChain, userPaths, requestURI)) {
        //     return;
        // }
        customerFilter(request, response, filterChain);
        // filterChain.doFilter(request, response);
    }

    private boolean accessUserPath(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
        String[] userPaths, String requestURI) throws ServletException, IOException {
        if (userPaths != null) {
            for (String path : userPaths) {
                if (requestURI.matches(path)) {
                    customerFilter(request, response, filterChain);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean accessManagerPath(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
        String[] superAdminPaths, String requestURI) throws ServletException, IOException {
        if (superAdminPaths != null) {
            for (String path : superAdminPaths) {
                if (requestURI.matches(path)) {
                    managerFilter(request, response, filterChain);
                    return true;
                }
            }
        }
        return false;
    }

    private void managerFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
        ServletException,
        IOException {
        log.info("managerFilter");
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("token null");
            filterChain.doFilter(request, response);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {

            log.info("Token is expired");
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getLoginId(token);
        String role = jwtUtil.getRole(token);

        Manager manager = Manager.builder()
            .loginId(username)
            .role(Role.valueOf(role))
            .build();

        ManagerUserDetails customUserDetails = new ManagerUserDetails(manager);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
            customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private void customerFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws
        IOException,
        ServletException {

        log.info("customerFilter");
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
