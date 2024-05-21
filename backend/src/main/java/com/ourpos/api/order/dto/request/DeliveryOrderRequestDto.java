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
    private DeliveryAddressDto deliveryAddress;
    private String ownerMessage;
    private String riderMessage;
    private Integer tip;
    private Boolean disposableYn;
    private List<OrderDetailRequestDto> orderDetails = new ArrayList<>();

    public DeliveryOrder toEntity(Customer customer, Store store, List<OrderDetail> orderDetails) {
        return DeliveryOrder.builder()
            .customer(customer)
            .store(store)
            .orderAddress(deliveryAddress.toEntity())
            .ownerMessage(ownerMessage)
            .riderMessage(riderMessage)
            .tip(tip)
            .disposableYn(disposableYn)
            .orderDetails(orderDetails)
            .build();
    }
}
