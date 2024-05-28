package com.ourpos.api.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.category.dto.request.CategoryRequestDto;
import com.ourpos.api.category.dto.request.CategoryUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionUpdateDto;
import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.CategoryRepository;
import com.ourpos.domain.menu.MenuOption;
import com.ourpos.domain.menu.MenuOptionGroup;
import com.ourpos.domain.menu.MenuOptionGroupRepository;
import com.ourpos.domain.menu.MenuOptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
	public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
	private final CategoryRepository categoryRepository;
	private final MenuOptionGroupRepository menuOptionGroupRepository;
	private final MenuOptionRepository menuOptionRepository;

	@Override
	public void addCategory(CategoryRequestDto categoryRequestDto) {
		log.info("CategoryService.addCategroy() called");

		Category category = categoryRequestDto.toEntity();
		categoryRepository.save(category);
	}

	@Override
	public void addMenuOptionGroup(MenuOptionGroupRequestDto menuOptionGroupRequestDto) {
		log.info("CategoryService.addMenuOptionGroup() called");
		// List<MenuOptionGroup> groups = categoryRequestDto.toEntity().getMenuOptionGroups();
		// for (MenuOptionGroup group : groups) {
		// 	menuOptionGroupRepository.save(group);
		// }
		Category insertCategory = categoryRepository.findById(menuOptionGroupRequestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException("카테고리가 존재하지 않습니다"));

		MenuOptionGroup menuOptionGroup = menuOptionGroupRequestDto.toEntity(insertCategory);
		menuOptionGroupRepository.save(menuOptionGroup);
	}

	@Override
	public void addMenuOption(MenuOptionRequestDto menuOptionRequestDto) {
		log.info("CategoryService.addCategroy() called");
		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(
			menuOptionRequestDto.getMenuOptionGroupId()).orElseThrow(
			() -> new IllegalArgumentException("메뉴옵션그룹이 존재하지 않습니다"));
		MenuOption menuOption = menuOptionRequestDto.toEntity(menuOptionGroup);
		menuOptionRepository.save(menuOption);
	}

	@Override
	public void updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto) {
		log.info("CategoryService.updateCategory() called");
		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException(CATEGORY_NOT_FOUND_MESSAGE));
		category.update(categoryUpdateDto.getName());
	}

	@Override
	public void updateMenuOptionGroup(Long menuOptionGroupId, MenuOptionGroupUpdateDto menuOptionGroupUpdateDto) {
		log.info("CategoryService.updateMenuOptionGroup() called");
		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId).orElseThrow(
			() -> new IllegalArgumentException("메뉴옵션그룹이 존재하지 않습니다"));
		Category category = categoryRepository.findById(menuOptionGroupUpdateDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException("카테고리가 존재하지 않습니다"));
		menuOptionGroup.update(category, menuOptionGroupUpdateDto.getName(), menuOptionGroupUpdateDto.getExclusiveYn(),
			menuOptionGroupUpdateDto.getDescription());
		log.info(menuOptionGroup.getName());
	}

	@Override
	public void updateMenuOption(Long menuOptionId, MenuOptionUpdateDto menuOptionUpdateDto) {
		log.info("MenuService.updateMenuOption() called");
		MenuOption menuOption = menuOptionRepository.findById(menuOptionId).orElseThrow(
			() -> new IllegalArgumentException("메뉴옵션이 존재하지 않습니다"));
		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionId)
			.orElseThrow(
				() -> new IllegalArgumentException("메뉴옵션그룹이 존재하지 않습니다"));
		menuOption.update(menuOptionGroup, menuOptionUpdateDto.getName(), menuOptionUpdateDto.getPrice());
	}

	@Override
	public void deleteCategory(Long categoryId) {
		log.info("CategoryService.deleteCategory() called");
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public void deleteMenuOptionGroup(Long menuOptionGroupId) {
		log.info("CategoryService.deleteMenuOptionGroup() called");
		menuOptionGroupRepository.deleteById(menuOptionGroupId);
	}

	@Override
	public void deleteMenuOption(Long menuOptioinId) {
		log.info("CategoryService.deleteMenuOption() called");
		menuOptionRepository.deleteById(menuOptioinId);
	}
}
