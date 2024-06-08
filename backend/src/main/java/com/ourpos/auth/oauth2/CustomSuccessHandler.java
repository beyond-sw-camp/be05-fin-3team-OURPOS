package com.ourpos.auth.oauth2;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ourpos.auth.dto.CustomOAuth2Customer;
import com.ourpos.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2Customer customCustomerDetails = (CustomOAuth2Customer)authentication.getPrincipal();

        String loginId = customCustomerDetails.getLoginId();
        Collection<? extends GrantedAuthority> authorities = customCustomerDetails.getAuthorities();
        String role = authorities.stream().findFirst().get().getAuthority();

        String token = jwtUtil.createJwt(loginId, role, 1000L * 60 * 60 * 24 * 7);

        response.addCookie(createCookie("Authorization", token));
        // response.sendRedirect("http://localhost:3000/");
        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("http://localhost:3000/admin");
        } else {
            response.sendRedirect("http://localhost:3000/");
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);

        // cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        cookie.setSecure(true);

        return cookie;
    }

}
