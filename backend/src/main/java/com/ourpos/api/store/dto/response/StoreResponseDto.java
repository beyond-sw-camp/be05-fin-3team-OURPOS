package com.ourpos.api.store.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jetbrains.annotations.NotNull;

import com.ourpos.api.map.Duration;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreResponseDto implements Comparable<StoreResponseDto> {

    private Long storeId;
    private StoreAddressResponseDto addressResponseDto;
    private String storeName;
    private String storePhone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minimumOrderPrice;
    private String pictureUrl;
    private Boolean closeYn;
    private LocalDateTime closedDateTime;
    private double distance;
    private double duration;

    public StoreResponseDto(Store store, Duration duration) {
        this.storeId = store.getId();
        this.addressResponseDto = new StoreAddressResponseDto(store.getAddress());
        this.storeName = store.getName();
        this.storePhone = store.getPhone();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minimumOrderPrice = store.getMinimumOrderPrice();
        this.pictureUrl = store.getPictureUrl();
        this.closeYn = store.getCloseYn();
        this.closedDateTime = store.getClosedDateTime();
        this.distance = duration.distance();
        this.duration = duration.duration();
    }

    @Override
    public int compareTo(@NotNull StoreResponseDto o) {
        return Double.compare(this.distance, o.distance);
    }
}
