package com.nazir.pos.posconfig;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.posconfig.dto.PosConfigRequest;
import com.nazir.pos.posconfig.dto.PosConfigResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manage/pos-config")
@RequiredArgsConstructor
public class PosConfigController {

    private final PosConfigService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PosConfigResponse> createOrUpdate(@Valid @RequestBody PosConfigRequest request, Authentication authentication) {
        CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveOrUpdate(request, admin.getStoreId(),
                admin.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PosConfigResponse>> configDetail(Authentication authentication) {
        CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(service.configDtl(admin.getStoreId()));
    }

}
