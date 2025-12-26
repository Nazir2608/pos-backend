package com.nazir.pos.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository
        extends JpaRepository<InventoryTransaction, Long> {

    @Query("""
                SELECT COALESCE(SUM(
                    CASE WHEN i.type = 'IN' THEN i.quantity
                         WHEN i.type = 'OUT' THEN -i.quantity
                    END
                ), 0)
                FROM InventoryTransaction i
                WHERE i.productId = :productId
                  AND i.storeId = :storeId
            """)
    Integer getAvailableStock(Long productId, Long storeId);

    List<InventoryTransaction> findByProductIdAndStoreId(
            Long productId, Long storeId);
}
