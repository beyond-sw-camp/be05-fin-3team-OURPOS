package com.ourpos.api.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ourpos.api.menu.dto.request.MenuRequestDto;

@Service
public interface MenuService {

	public void addMenu(MenuRequestDto menuRequestDto, MultipartFile nultipartfile);
}
