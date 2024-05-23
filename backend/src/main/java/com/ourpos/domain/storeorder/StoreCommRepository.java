package com.ourpos.domain.storeorder;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ourpos.domain.store.Store;

public interface StoreCommRepository extends JpaRepository<StoreComm, Long>{
}
