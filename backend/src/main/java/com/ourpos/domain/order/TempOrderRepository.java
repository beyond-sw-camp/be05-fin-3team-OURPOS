package com.ourpos.domain.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempOrderRepository extends JpaRepository<TempOrder, Long> {

    Optional<TempOrder> findByTempOrderId(String tempOrderId);
}
