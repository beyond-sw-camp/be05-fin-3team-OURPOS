package com.ourpos.domain.storeorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StoreMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_menu_id")
    private Long id;

    @Column(name = "store_menu_name")
    private String name;

    @Column(name = "store_menu_price")
    private Integer price;

    @Column(name = "store_menu_article_unit")
    private String articleUnit;

    @Column(name = "store_menu_status")
    @Enumerated(EnumType.STRING)
    private StoreMenuStatus status;

    @Column(name = "store_menu_description")
    private String description;

    @Column(name = "store_menu_category")
    @Enumerated(EnumType.STRING)
    private StoreMenuCategory category;

    @Column(name = "store_menu_picture_url")
    private String pictureUrl;

    @Column(name = "store_menu_deleted_yn")
    private Boolean deletedYn;

    @Column(name = "store_menu_deleted_datetime")
    private String deletedDatetime;

    @Builder
    private StoreMenu(String name, Integer price, String articleUnit, String description, StoreMenuCategory category,
        String pictureUrl) {
        this.name = name;
        this.price = price;
        this.articleUnit = articleUnit;
        this.description = description;
        this.category = category;
        this.pictureUrl = pictureUrl;
        this.status = StoreMenuStatus.WAITING;
        this.deletedYn = false;
    }
}
