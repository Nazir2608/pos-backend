package com.nazir.pos.user.dto;

import com.nazir.pos.user.Role;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private Role role;   // CASHIER or MANAGER
    private Long storeId;
}
