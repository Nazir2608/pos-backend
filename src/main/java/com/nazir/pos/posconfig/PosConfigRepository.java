package com.nazir.pos.posconfig;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PosConfigRepository extends JpaRepository<PosConfig, Long> {

    Optional<PosConfig> findByIdAndStoreId(Long id, Long storeId);

    List<PosConfig> findByStoreId(Long storeId);
}
