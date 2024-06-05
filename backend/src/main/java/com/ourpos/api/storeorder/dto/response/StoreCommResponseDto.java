package com.ourpos.api.storeorder.dto.response;

import java.time.LocalDateTime;

import com.ourpos.domain.storeorder.StoreComm;
import com.ourpos.domain.storeorder.StoreCommCategory;
import com.ourpos.domain.storeorder.StoreCommStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Schema(description = "주문 가능한 비품/ 식자재 리스트 ResponseDto")
public class StoreCommResponseDto {

    @Schema(description = "주문 가능한 가게 물품의 아이디")
    private Long storeCommId;

    @Schema(description = "주문 가능한 가게 물품의 이름")
    private String storeCommName;

    @Schema(description = "주문 가능한 가게 물품의 가격")
    private Integer storeCommPrice;

    @Schema(description = "주문 가능한 가게 물품의 단위")
    private String storeCommArticleUnit;

    //private StoreCommStatus storeCommStatus;

    @Schema(description = "주문 가능한 가게 물품에 대한 상세 설명")
    private String storeCommDescription;
    @Schema(description = "주문 가능한 가게 물품의 카테고리")
    private StoreCommCategory storeCommCategory;
    @Schema(description = "주문 가능한 가게 물품의 사진")
    private String storeCommPictureUrl;

    //private LocalDateTime storeCommCreatedDateTime;
    //private LocalDateTime storeCommModifiedDateTime;

    @Schema(description = "가게 물품의 주문 가능 여부 (주문 가능한 가게 물품의 삭제 여부)")
    private Boolean storeCommDeleteYn;
    //private LocalDateTime storeCommDeletedDateTime;

    public StoreCommResponseDto(StoreComm storeComm) {
        this.storeCommId = storeComm.getId();
        this.storeCommName = storeComm.getName();
        this.storeCommPrice = storeComm.getPrice();
        this.storeCommArticleUnit = storeComm.getArticleUnit();
        //this.storeCommStatus = storeComm.getStatus();
        this.storeCommDescription = storeComm.getDescription();
        this.storeCommCategory = storeComm.getCategory();
        this.storeCommPictureUrl = storeComm.getPictureUrl();
        //this.storeCommCreatedDateTime = storeComm.getCreatedDateTime();
        //this.storeCommModifiedDateTime = storeComm.getModifiedDateTime();
        this.storeCommDeleteYn = storeComm.getDeletedYn();
        //this.storeCommDeletedDateTime = storeComm.getDeletedDatetime();
    }
}