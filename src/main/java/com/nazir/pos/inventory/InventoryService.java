package com.nazir.pos.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public void addStock(Long productId, Long storeId, int quantity, String reference) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        inventoryRepository.save(
                InventoryTransaction.builder()
                        .productId(productId)
                        .storeId(storeId)
                        .type(InventoryType.IN)
                        .quantity(quantity)
                        .reference(reference)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        log.info("INVENTORY | Stock IN productId={}, qty={}, storeId={}", productId, quantity, storeId);
    }

    public void removeStock(Long productId, Long storeId, int quantity, String reference) {
        Integer available = inventoryRepository.getAvailableStock(productId, storeId);
        if (available < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        inventoryRepository.save(
                InventoryTransaction.builder()
                        .productId(productId)
                        .storeId(storeId)
                        .type(InventoryType.OUT)
                        .quantity(quantity)
                        .reference(reference)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        log.info("INVENTORY | Stock OUT productId={}, qty={}, storeId={}", productId, quantity, storeId);
    }

    public Integer getAvailableStock(Long productId, Long storeId) {
        return inventoryRepository.getAvailableStock(productId, storeId);
    }
}