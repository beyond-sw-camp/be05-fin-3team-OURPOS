package com.ourpos.auth.jwt;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.dto.customer.CustomerLoginDto;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.customer.Role;
import com.ourpos.domain.manager.Manager;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Map<String, String[]> hasRoleUser = Map.of(
        "GET", new String[] {"/api/v1/customers/**", "api/v1/stores/delivery", "/api/v1/orders/delivery",
            "/api/v1/orders/hall"},
        "POST", new String[] {"/api/v1/customers/**"},
        "PUT", new String[] {"/api/v1/customers/**"}
    );

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String[] userPaths = hasRoleUser.get(method);

        String managerToken = getManagerToken(request);
        if (managerToken != null) {
            if (accessUserPath(userPaths, requestURI)) {
                log.info("userPath");
                customerFilter(request, response, filterChain);
                return;
            }
            log.info("managerToken");
            managerFilter(request, response, filterChain);
            return;
        }

        String customerToken = getCustomerToken(request);
        if (customerToken != null) {
            log.info("customerToken");
            customerFilter(request, response, filterChain);
            return;
        }

        log.info("no token");
        filterChain.doFilter(request, response);
    }

    private boolean accessUserPath(String[] userPaths, String requestURI) {
        if (userPaths != null) {
            for (String path : userPaths) {
                path = path.replace("**", "");
                if (requestURI.startsWith(path)) {
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
        String token = getManagerToken(request);

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

    private String getManagerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("token null");

            return null;
        }

        String token = authorization.split(" ")[1];

        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            log.info("Token is expired");

            return null;
        } catch (SignatureException e) {
            log.info("Token is invalid");

            return null;
        }
        return token;
    }

    private void customerFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws
        IOException,
        ServletException {

        log.info("customerFilter");
        String token = getCustomerToken(request);
        if (token == null) {
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

    private @Nullable String getCustomerToken(HttpServletRequest request) {
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

            return null;
        }

        String token = authorization;
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            log.info("Token is expired");

            return null;
        } catch (SignatureException e) {
            log.info("Token is invalid");

            return null;
        }
        return token;
    }
}
