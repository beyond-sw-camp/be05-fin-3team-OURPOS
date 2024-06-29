package com.ourpos.domain.recipe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.storeorder.StoreComm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @JoinColumn(name = "store_comm_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreComm storeComm;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(name = "recipe_content")
    private Integer content;

    @Builder
    private Recipe(StoreComm storeComm, Menu menu, Integer content) {
        this.storeComm = storeComm;
        this.menu = menu;
        this.content = content;
    }

    public void setContent(Integer content) {
        this.content += content;
    }
}
