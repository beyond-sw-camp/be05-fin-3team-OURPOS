package com.ourpos.api.menu.service;

import java.nio.file.FileStore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.menu.request.MenuRequestDto;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuServiceImpl implements MenuService {
	public static final String MENU_NOT_FOUND_MESSAGE = "Menu not found";

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final FileStore fileStore;

	@Transactional
	public void addMenu(MenuRequestDto menuRequestDto, MultipartFile nultipartfile) {
		log.info("MenuService.addMenu() called");
		Store store = storeRepository.findById(menuRequestDto.getStoreId()).orElseThrow(
			() -> new IllegalArgumentException("Store not found"));

		addMenuPicture(menuRequestDto, multipartFile);

		Menu menu = Menu.builder()
			.name(menuRequestDto.getName())
			.price(menuRequestDto.getPrice())
			.build();
	}
}
