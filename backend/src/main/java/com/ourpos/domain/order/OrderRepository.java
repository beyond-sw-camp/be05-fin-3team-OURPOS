package com.ourpos.domain.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o "
        + "FROM HallOrder o "
        + "WHERE o.id = :orderId")
    Optional<HallOrder> findHallOrderById(Long orderId);
}
