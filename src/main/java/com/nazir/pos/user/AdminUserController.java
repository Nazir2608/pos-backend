package com.nazir.pos.user;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.user.dto.CreateUserRequest;
import com.nazir.pos.user.dto.UpdateUserStatusRequest;
import com.nazir.pos.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        log.info("user create request received for username={}", request.getUsername());
        return userService.createUser(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> listUsers(Authentication authentication) {
        CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();
        return userService.listUsers(admin.getStoreId());
    }
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserStatus(
            @PathVariable Long userId,
            @RequestBody UpdateUserStatusRequest request) {

        userService.updateUserStatus(userId, request.isEnabled());
    }

}
