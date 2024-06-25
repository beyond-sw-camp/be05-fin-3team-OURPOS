package com.ourpos.domain.storeorder;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
	
	List<StoreOrder> findByStoreId(Long storeId);

	Page<StoreOrder> findByStoreId(Long storeId, Pageable pageable);

	Page<StoreOrder> findByStatusIn(List<StoreOrderStatus> statuses, Pageable pageable);

	@Query("SELECT so FROM StoreOrder so WHERE so.store.id = :storeId " +
		"AND so.status IN (:statuses)")
	Page<StoreOrder> findByStoreIdAndStatusIn(Long storeId, List<StoreOrderStatus> statuses, Pageable pageable);

}
