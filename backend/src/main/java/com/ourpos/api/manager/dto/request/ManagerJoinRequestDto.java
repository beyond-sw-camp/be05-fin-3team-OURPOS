package com.ourpos.api.manager.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerJoinRequestDto {

    private String loginId;
    private String password;

    public ManagerJoinRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
