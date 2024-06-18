package com.ourpos.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TempOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tempOrderId;
    private int amount;
    private boolean isConfirmed;

    @Builder
    public TempOrder(String tempOrderId, int amount, boolean isConfirmed) {
        this.tempOrderId = tempOrderId;
        this.amount = amount;
        this.isConfirmed = isConfirmed;
    }

    public void confirm() {
        this.isConfirmed = true;
    }
}
