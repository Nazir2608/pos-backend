package com.nazir.pos.category;

import com.nazir.pos.auth.CustomUserDetails;
import com.nazir.pos.category.dto.CategoryResponse;
import com.nazir.pos.category.dto.CreateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ADMIN & MANAGER can create categories
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request,
                                                           Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request, user.getStoreId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<CategoryResponse>> listCategories(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(categoryService.listCategories(user.getStoreId()));
    }

    @PatchMapping("/{categoryId}/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> disableCategory(@PathVariable Long categoryId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        categoryService.disableCategory(categoryId, user.getStoreId());
        return ResponseEntity.ok(Map.of("message", "Category disabled successfully"));
    }

    @PatchMapping("/{categoryId}/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> enableCategory(@PathVariable Long categoryId, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        categoryService.enableCategory(categoryId, user.getStoreId());
        return ResponseEntity.ok(Map.of("message", "Category enabled successfully"));
    }

}
