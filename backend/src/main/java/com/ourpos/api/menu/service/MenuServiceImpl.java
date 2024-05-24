package com.ourpos.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.file.FileStore;
import com.ourpos.api.file.UploadFile;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;
import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.CategoryRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
	public static final String MENU_NOT_FOUND_MESSAGE = "Menu not found";

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final FileStore fileStore;

	@Override
	public void addMenu(MenuRequestDto menuRequestDto, MultipartFile multipartFile) {
		log.info("MenuService.addMenu() called");

		// Store store = storeRepository.findById(menuRequestDto.getStoreId()).orElseThrow(
		// 	() -> new IllegalArgumentException("Store not found"));

		Category category = categoryRepository.findById(menuRequestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException("Category not found"));

		addMenuPicture(menuRequestDto, multipartFile);

		// Menu menu = menuRequestDto.toEntity(store, category);
		Menu menu = menuRequestDto.toEntity(category);

		menuRepository.save(menu);
	}

	private void addMenuPicture(MenuRequestDto menuRequestDto, MultipartFile multipartFile) {
		if (multipartFile != null) {
			UploadFile uploadFile = fileStore.storeFile(multipartFile);
			menuRequestDto.setPictureUrl(uploadFile.getStoreFilename());
		} else {
			menuRequestDto.setPictureUrl("default.png");
		}
	}

	@Transactional
	public void updateMenu(Long menuId, MenuUpdateDto menuUpdateDto) {
		log.info("MenuService.updateMenu() called");
		Menu menu = menuRepository.findById(menuId).orElseThrow(
			() -> new IllegalArgumentException(MENU_NOT_FOUND_MESSAGE));
		Category category = categoryRepository.findById(menuUpdateDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException("카테고리가 존재하지 않습니다"));

		menu.update(category, menuUpdateDto.getName(), menuUpdateDto.getPrice(),
			menuUpdateDto.getDescription(), menuUpdateDto.getPictureUrl(), menuUpdateDto.getMenuOptionGroups());
	}

	@Transactional
	public void deleteMenu(Long menuId) {
		log.info("MenuService.deleteMenu() called");
		menuRepository.deleteById(menuId);
	}

}
