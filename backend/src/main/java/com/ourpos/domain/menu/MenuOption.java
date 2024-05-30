package com.ourpos.domain.menu;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_option")
public class MenuOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_option_id")
	private Long id;

	@Setter
	@JoinColumn(name = "menu_option_group_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MenuOptionGroup menuOptionGroup;

	@Column(name = "menu_option_name")
	private String name;

	@Column(name = "menu_option_price")
	private Integer price;

	@Column(name = "menu_option_deleted_yn")
	private Boolean deletedYn;

	@Column(name = "menu_option_deleted_datetime")
	private LocalDateTime deletedDateTime;

	@Builder
	private MenuOption(String name, Integer price, MenuOptionGroup menuOptionGroup) {
		this.menuOptionGroup = menuOptionGroup;
		this.name = name;
		this.price = price;
		this.deletedYn = false;
	}

	public void update(MenuOptionGroup menuOptionGroup, String name, Integer price) {
		this.menuOptionGroup = menuOptionGroup;
		this.name = name;
		this.price = price;
	}

	public void delete(LocalDateTime deletedDateTime) {
		this.deletedYn = true;
		this.deletedDateTime = deletedDateTime;
	}
}
