package com.ourpos.api.order.service;

import org.springframework.stereotype.Service;

import com.ourpos.domain.order.OrderQueryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;
}
