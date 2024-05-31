package com.ourpos.domain.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ourpos.domain.store.Store;

public interface StoreRestrictedMenuRepository extends JpaRepository<StoreRestrictedMenu, Long> {

    List<StoreRestrictedMenu> findByStoreId(Long storeId);
    List<StoreRestrictedMenu> findByStore(Store store);
}
