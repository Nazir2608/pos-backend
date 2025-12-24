package com.nazir.pos.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCaseAndStoreId(String name, Long storeId);

    List<Category> findByStoreId(Long storeId);

    Optional<Category> findByIdAndStoreId(Long id, Long storeId);
}
