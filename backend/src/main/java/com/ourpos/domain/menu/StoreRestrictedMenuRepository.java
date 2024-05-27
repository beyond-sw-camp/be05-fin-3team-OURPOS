package com.ourpos.domain.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRestrictedMenuRepository extends JpaRepository<StoreRestrictedMenu, Long> {

    List<StoreRestrictedMenu> findByStoreId(Long store_id);
}
