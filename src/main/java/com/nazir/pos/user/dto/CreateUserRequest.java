package com.nazir.pos.user.dto;

import com.nazir.pos.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100)
    private String password;

    @NotNull(message = "Role is required")
    private Role role;   // CASHIER / MANAGER

    @NotNull(message = "StoreId is required")
    private Long storeId;
}
