package com.ourpos.domain.orderdetail;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_group_id")
    private Long id;

    @Setter
    @JoinColumn(name = "order_detail_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OrderDetail orderDetail;

    @Column(name = "order_option_group_name")
    private String name;

    @OneToMany(mappedBy = "orderOptionGroup")
    private List<OrderOption> orderOptions = new ArrayList<>();

    @Builder
    public OrderOptionGroup(String name, OrderOption... orderOptions) {
        this.name = name;
        for (OrderOption orderOption : orderOptions) {
            addOrderOption(orderOption);
        }
    }

    // 연관관계 편의 메서드
    public void addOrderOption(OrderOption orderOption) {
        orderOptions.add(orderOption);
        orderOption.setOrderOptionGroup(this);
    }
}
