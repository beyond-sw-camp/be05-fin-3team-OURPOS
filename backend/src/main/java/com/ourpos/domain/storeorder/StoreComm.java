package com.ourpos.domain.storeorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_comm")
public class StoreComm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_comm_id")
    private Long id;

    @Column(name = "store_comm_name")
    private String name;

    @Column(name = "store_comm_price")
    private Integer price;

    @Column(name = "store_comm_article_unit")
    private String articleUnit;

    @Column(name = "store_comm_status")
    @Enumerated(EnumType.STRING)
    private StoreCommStatus status;

    @Column(name = "store_comm_description")
    private String description;

    @Column(name = "store_comm_category")
    @Enumerated(EnumType.STRING)
    private StoreCommCategory category;

    @Column(name = "store_comm_picture_url")
    private String pictureUrl;

    @Column(name = "store_comm_deleted_yn")
    private Boolean deletedYn;

    @Column(name = "store_comm_deleted_datetime")
    private String deletedDatetime;

    @Builder
    private StoreComm(String name, Integer price, String articleUnit, String description, StoreCommCategory category,
        String pictureUrl) {
        this.name = name;
        this.price = price;
        this.articleUnit = articleUnit;
        this.description = description;
        this.category = category;
        this.pictureUrl = pictureUrl;
        this.status = StoreCommStatus.WAITING;
        this.deletedYn = false;
    }
}
