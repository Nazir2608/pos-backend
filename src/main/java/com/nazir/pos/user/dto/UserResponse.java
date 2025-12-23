package com.nazir.pos.user.dto;

import com.nazir.pos.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private Long storeId;
    private boolean enabled;
}
