package com.nazir.pos.user;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.user.dto.CreateUserRequest;
import com.nazir.pos.user.dto.UpdateUserStatusRequest;
import com.nazir.pos.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers(Authentication authentication) {
        CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(userService.listUsers(admin.getStoreId()));
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long userId,
                                                 @Valid @RequestBody UpdateUserStatusRequest request) {
        userService.updateUserStatus(userId, request.getEnabled());
        return ResponseEntity.noContent().build();
    }
}
