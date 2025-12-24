package com.nazir.pos.product;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.product.dto.CreateProductRequest;
import com.nazir.pos.product.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, user.getStoreId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ProductResponse>> listProducts(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(productService.listProducts(user.getStoreId()));
    }

    @PatchMapping("/{productId}/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> disableProduct(@PathVariable Long productId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        productService.disableProduct(productId, user.getStoreId());
        return ResponseEntity.ok(Map.of("message", "Product disabled successfully"));
    }

    @PatchMapping("/{productId}/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> enableProduct(@PathVariable Long productId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        productService.enableProduct(productId, user.getStoreId());
        return ResponseEntity.ok(Map.of("message", "Product enabled successfully"));
    }

}
