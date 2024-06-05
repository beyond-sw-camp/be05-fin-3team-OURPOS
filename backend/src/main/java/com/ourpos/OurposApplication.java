package com.ourpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class OurposApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurposApplication.class, args);
    }

}
