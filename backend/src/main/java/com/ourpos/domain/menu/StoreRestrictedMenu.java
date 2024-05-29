package com.ourpos.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_restricted_menu")
public class StoreRestrictedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_restricted_menu_id")
    private Long id;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Builder
    private StoreRestrictedMenu(Store store, Menu menu) {
        this.store = store;
        this.menu = menu;
    }
}
