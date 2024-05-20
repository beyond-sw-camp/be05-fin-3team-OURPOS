package com.ourpos.domain.menu;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "menu_option_group")
public class MenuOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_option_group_id")
    private Long id;

    @Setter
    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(name = "menu_option_group_name")
    private String name;

    @Column(name = "menu_option_group_exclusive_yn")
    private Boolean exclusiveYn;

    @Column(name = "menu_option_group_description")
    private String description;

    @OneToMany(mappedBy = "menuOptionGroup")
    private List<MenuOption> menuOptions = new ArrayList<>();

    @Builder
    private MenuOptionGroup(String name, Boolean exclusiveYn, String description,
        @Singular List<MenuOption> menuOptions) {
        this.name = name;
        this.exclusiveYn = exclusiveYn;
        this.description = description;
        for (MenuOption menuOption : menuOptions) {
            addMenuOption(menuOption);
        }
    }

    // 연관관계 메서드
    public void addMenuOption(MenuOption menuOption) {
        menuOptions.add(menuOption);
        menuOption.setMenuOptionGroup(this);
    }
}
