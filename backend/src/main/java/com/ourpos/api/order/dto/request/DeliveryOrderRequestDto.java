package com.ourpos.api.order.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.order.DeliveryOrder;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryOrderRequestDto {

    private Long customerId;
    private Long storeId;
    private OrderAddressRequestDto orderAddressRequestDto;
    private String ownerMessage;
    private String riderMessage;
    private Integer tip;
    private Boolean disposableYn;
    private List<OrderDetailRequestDto> orderDetailDtos = new ArrayList<>();

    public DeliveryOrder toEntity(Customer customer, Store store, List<OrderDetail> orderDetails) {
        return DeliveryOrder.builder()
            .customer(customer)
            .store(store)
            .orderAddress(orderAddressRequestDto.toEntity())
            .ownerMessage(ownerMessage)
            .riderMessage(riderMessage)
            .tip(tip)
            .disposableYn(disposableYn)
            .orderDetails(orderDetails)
            .build();
    }
}
