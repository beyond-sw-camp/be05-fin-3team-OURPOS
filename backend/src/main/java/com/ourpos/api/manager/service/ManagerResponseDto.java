package com.ourpos.api.manager.service;

import com.ourpos.domain.manager.Manager;

public class ManagerResponseDto {

    private String name;
    private String loginId;

    public ManagerResponseDto(Manager manager) {
        this.name = manager.getName();
        this.loginId = manager.getLoginId();
    }
}
