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
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_option_group")
public class OrderOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_group_id")
    private Long id;

    @Setter
    @JoinColumn(name = "order_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderDetail orderDetail;

    @Column(name = "order_option_group_name")
    private String name;

    @OneToMany(mappedBy = "orderOptionGroup", cascade = CascadeType.ALL)
    private List<OrderOption> orderOptions = new ArrayList<>();

    @Builder
    public OrderOptionGroup(String name, @Singular List<OrderOption> orderOptions) {
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

    public int calculatePrice() {
        return orderOptions.stream()
            .mapToInt(OrderOption::getPrice)
            .sum();
    }
}
