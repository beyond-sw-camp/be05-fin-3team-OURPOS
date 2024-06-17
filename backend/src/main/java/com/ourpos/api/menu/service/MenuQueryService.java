package com.ourpos.api.menu.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.menu.dto.response.MenuResponseDto;
import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.menu.Menu;
import com.ourpos.domain.menu.MenuQueryRepository;
import com.ourpos.domain.menu.StoreRestrictedMenu;
import com.ourpos.domain.menu.StoreRestrictedMenuQueryRepository;
import com.ourpos.domain.store.Store;
import com.ourpos.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class MenuQueryService {

    private final MenuQueryRepository menuQueryRepository;
    private final StoreRestrictedMenuQueryRepository storeRestrictedMenuQueryRepository;
    private final StoreRepository storeRepository;

    public static final String STORE_NOT_FOUND_MESSAGE = "해당 직영점이 존재하지 않습니다.";

    public MenuResponseDto findMenu(Long menuId) {
        Menu menu = menuQueryRepository.findOne(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        return new MenuResponseDto(menu);
    }

    public List<MenuResponseDto> findMenusByCategory(String category, Long storeId) {
        List<Menu> menus = menuQueryRepository.findAllWithCategory(category);
        List<StoreRestrictedMenu> storeRestrictedMenus = storeRestrictedMenuQueryRepository.findAllWithStoreId(storeId);
        List<MenuResponseDto> menusResult = new ArrayList<>();
        for (Menu menu : menus) {
            boolean isRestricted = false;
            for (StoreRestrictedMenu storeRestrictedMenu : storeRestrictedMenus) {
                if (menu.getId().equals(storeRestrictedMenu.getMenu().getId())) {
                    isRestricted = true;
                    break;
                }
            }
            MenuResponseDto menuResponseDto = new MenuResponseDto(menu);
            menuResponseDto.setAvailable(!isRestricted);
            menusResult.add(menuResponseDto);
        }
        return menusResult;
    }

    public List<MenuResponseDto> findMenusByCategoryStore(String category) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagerUserDetails managerUserDetails = (ManagerUserDetails)principal;
        String managerLoginId = managerUserDetails.getUsername();

        Store store = storeRepository.findByManagerLoginId(managerLoginId).orElseThrow(
            () -> new IllegalArgumentException(STORE_NOT_FOUND_MESSAGE));
        Long storeId = store.getId();

        List<Menu> menus = menuQueryRepository.findAllWithCategory(category);
        List<StoreRestrictedMenu> storeRestrictedMenus = storeRestrictedMenuQueryRepository.findAllWithStoreId(storeId);
        List<MenuResponseDto> menusResult = new ArrayList<>();
        for (Menu menu : menus) {
            boolean isRestricted = false;
            for (StoreRestrictedMenu storeRestrictedMenu : storeRestrictedMenus) {
                if (menu.getId().equals(storeRestrictedMenu.getMenu().getId())) {
                    isRestricted = true;
                    break;
                }
            }
            MenuResponseDto menuResponseDto = new MenuResponseDto(menu);
            menuResponseDto.setAvailable(!isRestricted);
            menusResult.add(menuResponseDto);
        }
        return menusResult;
    }

    public List<MenuResponseDto> findMenusByCategoryAdmin(String category) {
        List<Menu> menus = menuQueryRepository.findAllWithCategory(category);

        return menus.stream().map(MenuResponseDto::new).toList();
    }
}
