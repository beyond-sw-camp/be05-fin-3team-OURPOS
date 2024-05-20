package com.ourpos.api.order.request;

import java.util.ArrayList;
import java.util.List;

import com.ourpos.domain.customer.Customer;
import com.ourpos.domain.order.HallOrder;
import com.ourpos.domain.order.Order;
import com.ourpos.domain.orderdetail.OrderDetail;
import com.ourpos.domain.store.Store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OrderRequestDto {
    private Long customerId;
    private Long storeId;
    private Boolean orderTakeoutYn;
    private List<OrderDetailRequestDto> orderDetails = new ArrayList<>();

    public Order toEntity(Customer customer, Store store, List<OrderDetail> orderDetails) {
        return HallOrder.builder()
            .customer(customer)
            .store(store)
            .orderTakeoutYn(orderTakeoutYn)
            .orderDetails(orderDetails)
            .build();
    }

}
