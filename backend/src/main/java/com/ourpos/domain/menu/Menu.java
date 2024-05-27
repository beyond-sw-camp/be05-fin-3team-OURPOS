package com.ourpos.domain.menu;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_price")
    private Integer price;

    @Column(name = "menu_available_yn")
    private Boolean availableYn;

    @Column(name = "menu_description")
    private String description;

    @Column(name = "menu_picture_url")
    private String pictureUrl;

    @Column(name = "menu_deleted_yn")
    private Boolean deletedYn;

    @Column(name = "menu_deleted_datetime")
    private LocalDateTime deletedDateTime;

    @Builder
    private Menu(Store store, String name, Integer price, Category category, String description,
        String pictureUrl) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.availableYn = true;
        this.deletedYn = false;
    }

    public void update(Category category, String name, Integer price,
        String description, String pictureUrl) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    public void disabled() {
        this.availableYn = false;
    }

    public void activated() {
        this.availableYn = true;
    }
}
