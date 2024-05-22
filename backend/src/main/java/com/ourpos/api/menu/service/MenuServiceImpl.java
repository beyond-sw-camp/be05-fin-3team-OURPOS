package com.ourpos.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.file.FileStore;
import com.ourpos.api.file.UploadFile;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.CategoryRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.store.Store;
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

		Store store = storeRepository.findById(menuRequestDto.getStoreId()).orElseThrow(
			() -> new IllegalArgumentException("Store not found"));

		Category category = categoryRepository.findById(menuRequestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException("Category not found"));

		addMenuPicture(menuRequestDto, multipartFile);

		Menu menu = Menu.builder()
			.name(menuRequestDto.getMenuName())
			.price(menuRequestDto.getMenuPrice())
			.store(store)
			.category(category)
			.pictureUrl(menuRequestDto.getMenuPictureUrl())
			.build();

		menuRepository.save(menu);
	}

	private void addMenuPicture(MenuRequestDto menuRequestDto, MultipartFile multipartFile) {
		if (multipartFile != null) {
			UploadFile uploadFile = fileStore.storeFile(multipartFile);
			menuRequestDto.setMenuPictureUrl(uploadFile.getStoreFilename());
		} else {
			menuRequestDto.setMenuPictureUrl("default.png");
		}
	}

}
