package com.ourpos.auth.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.ourpos.auth.jwt.CustomerLogoutFilter;
import com.ourpos.auth.jwt.JwtFilter;
import com.ourpos.auth.jwt.JwtUtil;
import com.ourpos.auth.jwt.ManagerLoginFilter;
import com.ourpos.auth.oauth2.CustomClientRegistrationRepo;
import com.ourpos.auth.oauth2.CustomFailureHandler;
import com.ourpos.auth.oauth2.CustomSuccessHandler;
import com.ourpos.auth.service.CustomerOAuth2CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig {

    private final CustomerOAuth2CustomerService customerOAuth2CustomerService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                    List.of("https://m.ourpos.org", "https://admin.ourpos.org", "https://kiosk.ourpos.org"
                        , "http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);

                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }));

        http
            .csrf(AbstractHttpConfigurer::disable);

        http
            .formLogin(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> response.setStatus(401))
                .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403)));

        http
            .httpBasic(AbstractHttpConfigurer::disable);

        // 기존의 JWT 필터를 OAuth2LoginAuthenticationFilter 뒤에 추가합니다.
        JwtFilter jwtFilter = new JwtFilter(jwtUtil);
        http.addFilterAfter(jwtFilter, OAuth2LoginAuthenticationFilter.class);

        // CustomerLogoutFilter를 LogoutFilter 앞에 추가합니다.
        CustomerLogoutFilter logoutFilter = new CustomerLogoutFilter(jwtUtil);
        http.addFilterBefore(logoutFilter, LogoutFilter.class);

        http
            .oauth2Login(oauth2 -> oauth2
                .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customerOAuth2CustomerService)
                )
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)
            );

        ManagerLoginFilter loginFilter =
            new ManagerLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);

        loginFilter.setFilterProcessesUrl("/managers/login");

        // UsernamePasswordAuthenticationFilter 대신 로그인 필터를 추가합니다.
        http
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/managers/join", "/managers/login", "/login", "/healthcheck", "/test/**", "images/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
