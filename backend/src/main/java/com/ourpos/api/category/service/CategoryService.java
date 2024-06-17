package com.ourpos.api.category.service;

import org.springframework.stereotype.Service;

import com.ourpos.api.category.dto.request.CategoryRequestDto;
import com.ourpos.api.category.dto.request.CategoryUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionGroupUpdateDto;
import com.ourpos.api.category.dto.request.MenuOptionRequestDto;
import com.ourpos.api.category.dto.request.MenuOptionUpdateDto;

@Service
public interface CategoryService {

    public void addCategory(CategoryRequestDto categoryRequestDto);

    public void addMenuOptionGroup(MenuOptionGroupRequestDto menuOptionGroupRequestDto);

    public void addMenuOption(MenuOptionRequestDto menuOptionRequestDto);

    public void updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto);

    public void updateMenuOptionGroup(Long menuOptionGroupId, MenuOptionGroupUpdateDto menuOptionGroupUpdateDto);

    public void updateMenuOption(Long menuOptionId, MenuOptionUpdateDto menuOptionUpdateDto);

    public void deleteCategory(Long categoryId);

    public void deleteMenuOptionGroup(Long menuOptionGroupId);

    public void deleteMenuOption(Long menuOption);
}
