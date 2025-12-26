package com.nazir.pos.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.nazir.pos.auth.JwtUtil.extractToken;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistRepository blacklistRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);
        // HttpOnly cookie (browser)
        Cookie cookie = new Cookie("POS_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok(
                LoginResponse.builder()
                        .token(token)
                        .role(user.getRole().name())
                        .storeId(user.getStoreId())
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractToken(request);
        if (token != null) {
            try {
                String jti = jwtUtil.extractTokenIdAllowExpired(token);
                if (jti != null) {
                    blacklistRepository.save(
                            new BlacklistedToken(jti, LocalDateTime.now())
                    );
                }
            } catch (Exception ex) {
                log.warn("Logout token ignored: {}", ex.getMessage());
            }
        }
        // Clear cookie
        Cookie cookie = new Cookie("POS_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
