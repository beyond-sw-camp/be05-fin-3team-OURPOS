package com.ourpos.api.order.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KioskOrderRequestDto {

    @NotNull(message = "주문을 진행할 매장을 선택해주세요.")
    private Boolean orderTakeoutYn;

    @NotNull(message = "주문 상세를 입력해주세요.")
    private List<OrderDetailRequestDto> orderDetailDtos = new ArrayList<>();

    public HallOrder toEntity(Store store, List<OrderDetail> orderDetails) {
        return HallOrder.builder()
            .customer(store.getKioskCustomer())
            .store(store)
            .orderTakeoutYn(orderTakeoutYn)
            .orderDetails(orderDetails)
            .build();
    }

}
