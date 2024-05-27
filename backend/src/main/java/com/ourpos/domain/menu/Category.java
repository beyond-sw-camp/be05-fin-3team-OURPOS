package com.ourpos.domain.menu;

import java.util.ArrayList;
import java.util.List;


import com.ourpos.domain.BaseEntity;

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
public class Category extends BaseEntity {

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

	public void addMenuOptionGroup(MenuOptionGroup menuOptionGroup) {
		this.menuOptionGroups.add(menuOptionGroup);
		menuOptionGroup.setCategory(this);
	}
}
