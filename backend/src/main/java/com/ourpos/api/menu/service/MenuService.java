package com.ourpos.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.menu.dto.request.MenuRequestDto;
import com.ourpos.api.menu.dto.request.MenuUpdateDto;

@Service
public interface MenuService {

    public void addMenu(MenuRequestDto menuRequestDto, MultipartFile multipartfile);

    public void deleteMenu(Long menuId);

    public void updateMenu(Long menuId, MenuUpdateDto menuUpdateDto, MultipartFile multipartFile);

}
