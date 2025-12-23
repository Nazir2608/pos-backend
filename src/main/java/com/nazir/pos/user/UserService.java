package com.nazir.pos.user;

import com.nazir.pos.user.dto.CreateUserRequest;
import com.nazir.pos.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("ADMIN role cannot be created");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .storeId(request.getStoreId())
                .build();

        User saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .role(saved.getRole())
                .storeId(saved.getStoreId())
                .build();
    }

    public List<UserResponse> listUsers(Long storeId) {

        return userRepository.findByStoreId(storeId)
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .storeId(user.getStoreId())
                        .enabled(user.isEnabled())
                        .build())
                .toList();
    }
    public void updateUserStatus(Long userId, boolean enabled) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot disable ADMIN user");
        }

        user.setEnabled(enabled);
        userRepository.save(user);
    }


}
