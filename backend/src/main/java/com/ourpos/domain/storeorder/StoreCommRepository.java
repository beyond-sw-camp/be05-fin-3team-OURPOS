package com.ourpos.domain.storeorder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCommRepository extends JpaRepository<StoreComm, Long> {
    Page<StoreComm> findAll(Pageable pageable);
    Page<StoreComm> findByCategory(StoreCommCategory category, Pageable pageable);
}
