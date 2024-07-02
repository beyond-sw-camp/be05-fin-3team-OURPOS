package com.ourpos.api.menu.service;

import java.time.LocalDateTime;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.file.FileStore;
import com.ourpos.api.file.UploadFile;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;
import com.ourpos.api.menu.dto.request.StoreRestrictedMenuRequestDto;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.menu.Category;
import com.ourpos.domain.menu.CategoryRepository;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuRepository;
import com.ourpos.domain.menu.StoreRestrictedMenu;
import com.ourpos.domain.menu.StoreRestrictedMenuRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
    public static final String MENU_NOT_FOUND_MESSAGE = "해당 메뉴가 존재하지 않습니다.";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "해당 카테고리가 존재하지 않습니다.";
    public static final String STORE_NOT_FOUND_MESSAGE = "해당 직영점이 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final FileStore fileStore;
    private final StoreRepository storeRepository;
    private final StoreRestrictedMenuRepository storeRestrictedMenuRepository;

    public void deactivateMenu(StoreRestrictedMenuRequestDto storeRestrictedMenuRequestDto) {
        log.info("MenuService,deactivateMenu() called");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;
        String managerLoginId = managerUserDetails.getUsername();

        Menu menu = menuRepository.findById(storeRestrictedMenuRequestDto.getMenuId()).orElseThrow(
            () -> new IllegalArgumentException(MENU_NOT_FOUND_MESSAGE));
        Store store = storeRepository.findByManagerLoginId(managerLoginId).orElseThrow(
            () -> new IllegalArgumentException(STORE_NOT_FOUND_MESSAGE));

        StoreRestrictedMenu storeRestrictedMenu = storeRestrictedMenuRequestDto.toEntity(menu, store);
        storeRestrictedMenuRepository.save(storeRestrictedMenu);
    }

    public void activateMenu(StoreRestrictedMenuRequestDto storeRestrictedMenuRequestDto) {
        log.info("MenuService,activateMenu() called");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;
        String managerLoginId = managerUserDetails.getUsername();

        Menu menu = menuRepository.findById(storeRestrictedMenuRequestDto.getMenuId()).orElseThrow(
            () -> new IllegalArgumentException(MENU_NOT_FOUND_MESSAGE));
        Store store = storeRepository.findByManagerLoginId(managerLoginId).orElseThrow(
            () -> new IllegalArgumentException(STORE_NOT_FOUND_MESSAGE));

        log.info("Initiate Deleting StoreRestrictedMenu with menuId: {} and storeId: {}", menu.getId(), store.getId());
        storeRestrictedMenuRepository.deleteByMenuIdAndStoreId(menu.getId(), store.getId());
        log.info("finish Deleting StoreRestrictedMenu with menuId: {} and storeId: {}", menu.getId(), store.getId());
    }

    @Override
    public void addMenu(MenuRequestDto menuRequestDto, MultipartFile multipartFile) {
        log.info("MenuService.addMenu() called");

        Category category = categoryRepository.findById(menuRequestDto.getCategoryId()).orElseThrow(
            () -> new IllegalArgumentException(CATEGORY_NOT_FOUND_MESSAGE));

        addMenuPicture(menuRequestDto, multipartFile);
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

    @Override
    public void updateMenu(Long menuId, MenuUpdateDto menuUpdateDto) {
        log.info("MenuService.updateMenu() called");
        Menu menu = menuRepository.findById(menuId)
            .filter(m -> !m.getDeletedYn())
            .orElseThrow(
                () -> new IllegalArgumentException(MENU_NOT_FOUND_MESSAGE));
        Category category = categoryRepository.findById(menuUpdateDto.getCategoryId()).orElseThrow(
            () -> new IllegalArgumentException(CATEGORY_NOT_FOUND_MESSAGE));

        menu.update(category, menuUpdateDto.getName(), menuUpdateDto.getPrice(),
            menuUpdateDto.getDescription());
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        log.info("MenuService.deleteMenu() called");
        Menu menu = menuRepository.findById(menuId).orElseThrow(
            () -> new IllegalArgumentException(MENU_NOT_FOUND_MESSAGE));

        menu.delete(LocalDateTime.now());
    }

}
