package com.nazir.pos.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Login request received for username={}", request.getUsername());
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()));

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .role(userDetails.getRole().name())
                .storeId(userDetails.getStoreId())
                .build();
    }
}
