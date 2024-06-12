package com.ourpos.auth.config;

import static org.springframework.http.HttpMethod.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import com.ourpos.auth.oauth2.CustomSuccessHandler;
import com.ourpos.auth.service.CustomerOAuth2CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    public static final Map<String, String[]> hasRoleSuperAdmin = Map.of(
        "GET", new String[] {},
        "POST", new String[] {},
        "PUT", new String[] {}
    );

    public static final Map<String, String[]> hasRoleAdmin = Map.of(
        "GET", new String[] {"/api/v1/orders/monthly/my", "/api/v1/orders/hall/my", "/api/v1/orders/delivery/my",
            "/api/v1/orders/monthly/my", "/api/v1/orders/meal-time/my", "/api/v1/orders/meal-time/my",
            "/api/v1/orders/menu-prefer/my", "/api/v1/orders/delivery/frequency/my"},
        "POST", new String[] {},
        "PUT", new String[] {}
    );

    public static final Map<String, String[]> hasRoleUser = Map.of(
        "GET", new String[] {"USER"},
        "POST", new String[] {"USER"},
        "PUT", new String[] {"USER"}
    );

    private final CustomerOAuth2CustomerService customerOAuth2CustomerService;
    private final CustomSuccessHandler customSuccessHandler;
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

                configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
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
            .formLogin(AbstractHttpConfigurer::disable);

        http
            .httpBasic(AbstractHttpConfigurer::disable);

        http
            .addFilterAfter(new JwtFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
            .addFilterBefore(new CustomerLogoutFilter(jwtUtil), LogoutFilter.class);

        http
            .oauth2Login(oauth2 -> oauth2
                .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customerOAuth2CustomerService)
                )
                .successHandler(customSuccessHandler)
            );

        ManagerLoginFilter loginFilter =
            new ManagerLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/managers/login");
        http
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(GET, hasRoleSuperAdmin.get("GET")).hasRole("SUPER_ADMIN")
                .requestMatchers(POST, hasRoleSuperAdmin.get("POST")).hasRole("SUPER_ADMIN")
                .requestMatchers(PUT, hasRoleSuperAdmin.get("PUT")).hasRole("SUPER_ADMIN")
                .requestMatchers(GET, hasRoleAdmin.get("GET")).hasRole("ADMIN")
                .requestMatchers(POST, hasRoleAdmin.get("POST")).hasRole("ADMIN")
                .requestMatchers(PUT, hasRoleAdmin.get("PUT")).hasRole("ADMIN")
                // .requestMatchers(GET, hasRoleUser.get("GET")).hasRole("USER")
                // .requestMatchers(POST, hasRoleUser.get("POST")).hasRole("USER")
                // .requestMatchers(PUT, hasRoleUser.get("PUT")).hasRole("USER")
                .requestMatchers("/managers/join", "/managers/login", "/login").permitAll()
                .anyRequest().authenticated());

        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

