package com.ourpos.api.manager.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.api.manager.dto.request.ManagerJoinRequestDto;
import com.ourpos.domain.customer.Role;
import com.ourpos.domain.manager.Manager;
import com.ourpos.domain.manager.ManagerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ManagerJoinService {

    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(ManagerJoinRequestDto requestDto) {
        String loginId = requestDto.getLoginId();
        validateDuplicateLoginId(loginId);

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        Manager manager = Manager.builder()
            .loginId(loginId)
            .password(encodedPassword)
            .role(Role.ROLE_ADMIN)
            .build();
        managerRepository.save(manager);
    }

    private void validateDuplicateLoginId(String loginId) {
        if (managerRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 로그인 아이디입니다.");
        }
    }

    public List<ManagerResponseDto> findManagers() {
        List<Manager> managers = managerRepository.findAll();

        return managers.stream().map(ManagerResponseDto::new).toList();
    }
}
