package com.ourpos.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository<T extends Order> extends JpaRepository<T, Long> {
}
