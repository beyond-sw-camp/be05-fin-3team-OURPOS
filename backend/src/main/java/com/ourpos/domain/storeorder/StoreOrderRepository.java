package com.ourpos.domain.storeorder;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
	List<StoreOrder> findByStoreId(Long storeId);
	Page<StoreOrder> findByStoreId(Long storeId, Pageable pageable);

}
