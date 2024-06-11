package com.ourpos.domain.store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s "
        + "FROM Store s "
        + "JOIN FETCH s.address")
    List<Store> findAllWithAddress();

    Optional<Store> findByCustomerId(Long customerId);
}
