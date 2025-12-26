package com.nazir.pos.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CORS for cookie-based auth
                .cors(Customizer.withDefaults())
                // Disable defaults
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // Stateless
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Authorization
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                        // Admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // POS Config
                        .requestMatchers("/api/admin/pos-config/**").hasRole("ADMIN")
                        // Category & Product management
                        .requestMatchers("/api/manage/categories/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/manage/products/**").hasAnyRole("ADMIN", "MANAGER")
                        // Billing
                        .requestMatchers("/api/cashier/billing/**").hasAnyRole("ADMIN", "CASHIER")
                        // SALES REPORTS
                        .requestMatchers("/api/reports/sales/summary").hasAnyRole("ADMIN", "MANAGER", "CASHIER")
                        .requestMatchers("/api/reports/sales/pos").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/reports/sales/cashier").hasAnyRole("ADMIN", "MANAGER")
                        // Everything else
                        .anyRequest().authenticated()
                )
                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
