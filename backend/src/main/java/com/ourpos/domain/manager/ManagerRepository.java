package com.ourpos.domain.manager;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByLoginId(String loginId);

    Optional<Manager> findByLoginId(String loginId);
}
