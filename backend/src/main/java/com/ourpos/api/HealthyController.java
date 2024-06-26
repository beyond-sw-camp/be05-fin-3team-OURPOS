package com.ourpos.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
