package com.nazir.pos.category;

import com.nazir.pos.category.dto.CategoryResponse;
import com.nazir.pos.category.dto.CreateCategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CreateCategoryRequest request, Long storeId) {
        log.info("CATEGORY | Create category name={}, storeId={}", request.getName(), storeId);
        if (categoryRepository.existsByNameIgnoreCaseAndStoreId(request.getName(), storeId)) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .storeId(storeId)
                .active(true)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    public List<CategoryResponse> listCategories(Long storeId) {
        return categoryRepository.findByStoreId(storeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void disableCategory(Long categoryId, Long storeId) {
        Category category = categoryRepository
                .findByIdAndStoreId(categoryId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setActive(false);
        categoryRepository.save(category);
        log.info("CATEGORY | Disabled categoryId={}, storeId={}", categoryId, storeId);
    }

    public void enableCategory(Long categoryId, Long storeId) {
        Category category = categoryRepository
                .findByIdAndStoreId(categoryId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setActive(true);
        categoryRepository.save(category);
        log.info("CATEGORY | enabled categoryId={}, storeId={}", categoryId, storeId);
    }


    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .build();
    }
}
