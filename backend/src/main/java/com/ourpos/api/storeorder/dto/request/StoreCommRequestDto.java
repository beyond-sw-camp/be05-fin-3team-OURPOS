package com.ourpos.api.storeorder.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommCategory;
import com.ourpos.domain.storeorder.StoreCommStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StoreCommRequestDto {

    private Long                storeCommId;
    private String              storeCommName;
    private Integer             storeCommPrice;
    private String              storeCommArticleUnit;
    private StoreCommStatus     storeCommStatus;
    private String              storeCommDescription;
    private StoreCommCategory   storeCommCategory;
    private String              storeCommPictureUrl;
    private LocalDateTime       storeCommCreatedDateTime;
    private LocalDateTime       storeCommModifiedDateTime;
    private Boolean             storeCommDeleteYn;
    private LocalDateTime       storeCommDeletedDateTime;


    // public StoreCommRequestDto(StoreComm storeComm){
    //     this.storeCommId = storeComm.getId();
    //     this.storeCommName = storeComm.getName();
    //     this.storeCommPrice = storeComm.getPrice();
    //     this.storeCommArticleUnit = storeComm.getArticleUnit();
    //     this.storeCommStatus = storeComm.getStatus();
    //     this.storeCommDescription = storeComm.getDescription();
    //     this.storeCommCategory = storeComm.getCategory();
    //     this.storeCommPictureUrl = storeComm.getPictureUrl();
    //     this.storeCommCreatedDateTime = storeComm.getCreatedDateTime();
    //     this.storeCommModifiedDateTime = storeComm.getModifiedDateTime();
    //     this.storeCommDeleteYn = storeComm.getDeletedYn();
    //     this.storeCommDeletedDateTime = storeComm.getDeletedDatetime();
    // }

    // dto -> entity
    public StoreComm toEntity() {
        return StoreComm.builder()
            .name(storeCommName)
            .price(storeCommPrice)
            .articleUnit(storeCommArticleUnit)
            .description(storeCommDescription)
            .category(storeCommCategory)
            .pictureUrl(storeCommPictureUrl)
            .build();
    }
}
