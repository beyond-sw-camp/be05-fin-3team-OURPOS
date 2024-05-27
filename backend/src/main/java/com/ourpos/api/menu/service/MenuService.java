package com.ourpos.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.menu.dto.request.MenuOptionGroupUpdateDto;
import com.ourpos.api.menu.dto.request.MenuOptionUpdateDto;
import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;

@Service
public interface MenuService {

    public void addMenu(MenuRequestDto menuRequestDto, MultipartFile nultipartfile);

    public void deleteMenu(Long menuId);

    public void updateMenu(Long menuId, MenuUpdateDto menuUpdateDto);

    public void updateMenuOptionGroup(Long menuOptionGroupId, MenuOptionGroupUpdateDto menuOptionGroupUpdateDto);

    public void updateMenuOption(Long menuOptionId, MenuOptionUpdateDto menuOptionUpdateDto);
}
