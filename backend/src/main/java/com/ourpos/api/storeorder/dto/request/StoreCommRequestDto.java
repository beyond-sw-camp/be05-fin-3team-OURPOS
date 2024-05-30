package com.ourpos.api.storeorder.dto.request;

import java.time.LocalDateTime;

import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommCategory;
import com.ourpos.domain.storeorder.StoreCommStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StoreCommRequestDto {

    @NotNull(message = "가게 상품 Id를 입력해주세요")
    @Positive(message = "1 이상의 상수를 입력해주세요")
    private Long storeCommId;

    @NotBlank(message = "가게 물품의 이름을 입력해주세요")
    private String storeCommName;

    @NotBlank(message = "가게 물품의 가격을 입력해주세요")
    private Integer storeCommPrice;

    @NotBlank(message = "가게 물품의 단위를 입력해주세요" )
    private String storeCommArticleUnit;

    private StoreCommStatus storeCommStatus;

    @NotNull(message = "가게 물품의 상세 설명을 입력해주세요 ")
    private String storeCommDescription;

    @NotBlank(message = "가게 물품의 종류를 입력해주세요" )
    private StoreCommCategory storeCommCategory;

    private String storeCommPictureUrl;

    private LocalDateTime storeCommCreatedDateTime;

    private LocalDateTime storeCommModifiedDateTime;

    private Boolean storeCommDeleteYn;


    private LocalDateTime storeCommDeletedDateTime;

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
