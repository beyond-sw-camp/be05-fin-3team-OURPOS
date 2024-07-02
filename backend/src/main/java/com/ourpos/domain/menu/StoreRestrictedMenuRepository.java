package com.ourpos.domain.menu;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.domain.store.Store;

public interface StoreRestrictedMenuRepository extends JpaRepository<StoreRestrictedMenu, Long> {

    List<StoreRestrictedMenu> findByStoreId(Long storeId);

    List<StoreRestrictedMenu> findByStore(Store store);

    @Modifying
    @Transactional
    @Query("DELETE FROM StoreRestrictedMenu s WHERE s.menu.id = :menuId AND s.store.id = :storeId")
    void deleteByMenuIdAndStoreId(@Param("menuId") Long menuId, @Param("storeId") Long storeId);
}
