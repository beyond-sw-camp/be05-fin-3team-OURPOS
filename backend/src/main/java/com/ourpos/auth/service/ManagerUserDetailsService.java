package com.ourpos.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourpos.auth.dto.manager.ManagerUserDetails;
import com.ourpos.domain.manager.Manager;
import com.ourpos.domain.manager.ManagerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ManagerUserDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByLoginId(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + ", 관리자를 찾을 수 없습니다."));

        return new ManagerUserDetails(manager);
    }
}
