package com.ourpos.auth.oauth2;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ourpos.auth.dto.customer.CustomOAuth2Customer;
import com.ourpos.auth.exception.LoginRequiredException;
import com.ourpos.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        CustomOAuth2Customer customCustomerDetails = (CustomOAuth2Customer)authentication.getPrincipal();

        String loginId = customCustomerDetails.getLoginId();
        Collection<? extends GrantedAuthority> authorities = customCustomerDetails.getAuthorities();

        String role = authorities.stream().findFirst().orElseThrow(
            () -> new LoginRequiredException("권한이 없습니다.")
        ).getAuthority();

        String token = jwtUtil.createJwt(loginId, role, 1000 * 60L * 60 * 24 * 7);

        if (customCustomerDetails.isNewUser() && role.equals("ROLE_USER")) {
            ResponseCookie cookie = createCookie(token);
            response.addHeader("Set-Cookie", cookie.toString());
            response.sendRedirect("https://m.ourpos.org/signup-success");
            return;
        }

        ResponseCookie cookie = createCookie(token);
        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect("https://m.ourpos.org");
    }

    private ResponseCookie createCookie(String value) {

        return ResponseCookie.from("Authorization", value)
            .httpOnly(true)
            .domain(".ourpos.org")
            .maxAge(1000 * 60 * 60 * 24 * 7)
            .sameSite("None")
            .path("/")
            .secure(true)
            .build();
    }

}
