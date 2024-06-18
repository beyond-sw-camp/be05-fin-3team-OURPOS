package com.ourpos.api.map;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ourpos.api.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin("localhost:3001")
public class MapController {

    private final MapService mapService;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/maps")
    public Result<List<MapResponseDto>> flaskMap(@RequestParam String addressBase) {
        log.info("예상 매출액 보고서 행정동 코드 요청", addressBase);
        List<MapResponseDto> dtos = mapService.searchAddress(addressBase);

        return new Result<>(HttpStatus.OK.value(), "도로명 주소가 행정동 코드로 변환되었습니다", dtos);
    }
}
