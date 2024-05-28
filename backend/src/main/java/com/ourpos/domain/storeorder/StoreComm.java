package com.ourpos.domain.storeorder;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.ourpos.api.storeorder.dto.request.StoreCommRequestDto;
import com.ourpos.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_comm")
public class StoreComm extends BaseEntity {

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
    private LocalDateTime deletedDatetime;



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

    public void updateFromDto(StoreCommRequestDto dto) {
        this.name = dto.getStoreCommName();
        this.price = dto.getStoreCommPrice();
        this.articleUnit = dto.getStoreCommArticleUnit();
        this.description = dto.getStoreCommDescription();
        this.category = dto.getStoreCommCategory();
        this.pictureUrl = dto.getStoreCommPictureUrl();
        // 필요한 경우 status와 deletedYn 등의 필드도 업데이트할 수 있습니다.
    }

    public void delete() {
        this.deletedYn = false;
        this.deletedDatetime = LocalDateTime.now();
    }

}
