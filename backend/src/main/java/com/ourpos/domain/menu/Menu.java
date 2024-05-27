package com.ourpos.domain.menu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import com.ourpos.domain.BaseEntity;
import com.ourpos.domain.store.Store;

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
import lombok.Singular;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long id;

	@JoinColumn(name = "store_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Store store;

	@JoinColumn(name = "category_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@Column(name = "menu_name")
	private String name;

	@Column(name = "menu_price")
	private Integer price;

	@Column(name = "menu_available_yn")
	private Boolean availableYn;

	@Column(name = "menu_description")
	private String description;

	@Column(name = "menu_picture_url")
	private String pictureUrl;

	@Column(name = "menu_deleted_yn")
	private Boolean deletedYn;

	@Column(name = "menu_deleted_datetime")
	private LocalDateTime deletedDateTime;

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
	private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

	@Builder
	private Menu(Store store, String name, Integer price, Boolean availableYn, Category category,
		String description, String pictureUrl, @Singular List<MenuOptionGroup> menuOptionGroups) {
		this.store = store;
		this.name = name;
		this.price = price;
		this.availableYn = availableYn;
		this.category = category;
		this.description = description;
		this.pictureUrl = pictureUrl;
		this.deletedYn = false;
		for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
			addMenuOptionGroup(menuOptionGroup);
		}
	}

	// 연관관계 편의 메서드
	// public void addMenuOptionGroup(MenuOptionGroup menuOptionGroup) {
	// 	this.menuOptionGroups.add(menuOptionGroup);
	// 	menuOptionGroup.setMenu(this);
	// }

	public void update(Category category, String name, Integer price,
		String description, String pictureUrl) {
		this.category = category;
		this.name = name;
		this.price = price;
		this.description = description;
		this.pictureUrl = pictureUrl;
	}
}
