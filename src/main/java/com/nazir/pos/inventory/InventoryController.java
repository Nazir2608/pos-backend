package com.nazir.pos.inventory;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.inventory.dto.StockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> stockIn(@Valid @RequestBody StockRequest request, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        inventoryService.addStock(
                request.getProductId(),
                user.getStoreId(),
                request.getQuantity(),
                request.getReference()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> stockOut(@Valid @RequestBody StockRequest request, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        inventoryService.removeStock(
                request.getProductId(),
                user.getStoreId(),
                request.getQuantity(),
                request.getReference()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/available/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Integer> getAvailableStock(@PathVariable Long productId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(inventoryService.getAvailableStock(productId, user.getStoreId()));
    }
}
