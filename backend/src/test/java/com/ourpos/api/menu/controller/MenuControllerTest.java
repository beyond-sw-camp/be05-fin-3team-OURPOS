package com.ourpos.api.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ourpos.api.menu.service.MenuQueryService;
import com.ourpos.api.menu.service.MenuServiceImpl;

@WebMvcTest(controllers = MenuController.class)
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuQueryService menuQueryService;

    @MockBean
    MenuServiceImpl menuServiceImpl;

}