package com.nazir.pos.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<BlacklistedToken, String> {
}
