package com.nazir.pos.user;

import com.nazir.pos.user.dto.CreateUserRequest;
import com.nazir.pos.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        log.info("USER | Create user request username={}, role={}, storeId={}",
                request.getUsername(), request.getRole(), request.getStoreId());

        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("ADMIN role cannot be created");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .storeId(request.getStoreId())
                .enabled(true)
                .build();

        User saved = userRepository.save(user);

        log.info("USER | User created id={}, username={}", saved.getId(), saved.getUsername());

        return mapToResponse(saved);
    }

    public List<UserResponse> listUsers(Long storeId) {

        log.debug("USER | Fetching users for storeId={}", storeId);

        return userRepository.findByStoreId(storeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void updateUserStatus(Long userId, boolean enabled) {

        log.info("USER | Updating status userId={}, enabled={}", userId, enabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("ADMIN user cannot be disabled");
        }

        user.setEnabled(enabled);
        userRepository.save(user);

        log.info("USER | User status updated userId={}, enabled={}", userId, enabled);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .storeId(user.getStoreId())
                .enabled(user.isEnabled())
                .build();
    }
}
