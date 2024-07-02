package com.ourpos.domain.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ourpos.domain.storeorder.StoreCommCategory;

public interface StoreStockRepository extends JpaRepository<StoreStock, Long> {

    List<StoreStock> findByStoreIdAndStoreCommId(Long storeId, Long storeCommId);

    @Query("SELECT ss FROM StoreStock ss WHERE ss.store.id = :storeId AND ss.storeComm.category = com.ourpos.domain.storeorder.StoreCommCategory.INGREDIENT")
    Page<StoreStock> findAllByStoreId(@Param("storeId") Long storeId, Pageable pageable);


    // Page<StoreStock> findAllByStoreId(Long storeId, Pageable pageable);
    
}
