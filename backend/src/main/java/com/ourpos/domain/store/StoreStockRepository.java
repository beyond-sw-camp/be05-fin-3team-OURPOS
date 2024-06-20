package com.ourpos.domain.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreStockRepository extends JpaRepository<StoreStock, Long> {

    List<StoreStock> findByStoreIdAndStoreCommId(Long storeId, Long storeCommId);
    Page<StoreStock> findAllByStoreId(Long storeId, Pageable pageable);

    
}
