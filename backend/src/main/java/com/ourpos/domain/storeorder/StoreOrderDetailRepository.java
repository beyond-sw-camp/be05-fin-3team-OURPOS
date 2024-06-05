package com.ourpos.domain.storeorder;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail, Long> {
	List<StoreOrderDetail> findByStoreOrderId(Long storeOrderId);
}

