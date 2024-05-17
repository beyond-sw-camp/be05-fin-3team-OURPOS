package com.ourpos.domain.orderdetail;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.order.Order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    @JoinColumn(name = "menu_id")
    @ManyToOne
    private Menu menu;

    @Column(name = "order_detail_quantity")
    private Integer quantity;

    @Column(name = "order_detail_price")
    private Integer price;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL)
    List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

    @Builder
    public OrderDetail(Menu menu, Integer quantity, Integer price, OrderOptionGroup... orderOptionGroups) {
        this.menu = menu;
        this.quantity = quantity;
        this.price = price;
        for (OrderOptionGroup orderOptionGroup : orderOptionGroups) {
            addOrderOptionGroup(orderOptionGroup);
        }
    }

    // 연관관계 편의 메서드
    public void addOrderOptionGroup(OrderOptionGroup orderOptionGroup) {
        orderOptionGroups.add(orderOptionGroup);
        orderOptionGroup.setOrderDetail(this);
    }
}
