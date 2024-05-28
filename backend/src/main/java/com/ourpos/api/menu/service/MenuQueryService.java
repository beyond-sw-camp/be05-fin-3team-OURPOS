package com.ourpos.api.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.menu.dto.response.MenuResponseDto;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuQueryRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuQueryService {

	private final MenuQueryRepository menuQueryRepository;

	public MenuResponseDto findMenu(Long menuId) {
		Menu menu = menuQueryRepository.findOne(menuId)
			.orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

		return new MenuResponseDto(menu);
	}

	public List<MenuResponseDto> findMenusByCategory(String category) {
		List<Menu> menus = menuQueryRepository.findAllWithCategory(category);

		return menus.stream()
			.map(MenuResponseDto::new)
			.toList();
	}
}
