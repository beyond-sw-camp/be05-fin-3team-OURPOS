package com.ourpos.domain.menu;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "category_name")
	private String name;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

	@Builder
	private Category(String name, @Singular List<MenuOptionGroup> menuOptionGroups) {
		this.name = name;
		for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
			addMenuOptionGroup(menuOptionGroup);
		}
	}

	// 연관관계 편의 메서드
	public void addMenuOptionGroup(MenuOptionGroup menuOptionGroup) {
		menuOptionGroups.add(menuOptionGroup);
		menuOptionGroup.setCategory(this);
	}

	public void update(String name) {
		this.name = name;
	}
}

