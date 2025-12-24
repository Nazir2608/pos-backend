package com.nazir.pos.product;

import com.nazir.pos.product.dto.CreateProductRequest;
import com.nazir.pos.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(CreateProductRequest request, Long storeId) {
        log.info("PRODUCT | Create product sku={}, storeId={}", request.getSku(), storeId);
        if (productRepository.existsBySkuAndStoreId(request.getSku(), storeId)) {
            throw new IllegalArgumentException("Product SKU already exists");
        }
        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .price(request.getPrice())
                .gstPercentage(request.getGstPercentage())
                .categoryId(request.getCategoryId())
                .storeId(storeId)
                .active(true)
                .build();
        return mapToResponse(productRepository.save(product));
    }

    public List<ProductResponse> listProducts(Long storeId) {
        return productRepository.findByStoreId(storeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void disableProduct(Long productId, Long storeId) {
        Product product = productRepository
                .findByIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
        log.info("PRODUCT | Disabled productId={}, storeId={}", productId, storeId);
    }

    public void enableProduct(Long productId, Long storeId) {
        Product product = productRepository
                .findByIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setActive(true);
        productRepository.save(product);
        log.info("PRODUCT | Enabled productId={}, storeId={}", productId, storeId);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .gstPercentage(product.getGstPercentage())
                .categoryId(product.getCategoryId())
                .active(product.isActive())
                .build();
    }
}
