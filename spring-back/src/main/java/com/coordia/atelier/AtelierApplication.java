package com.coordia.atelier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AtelierApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtelierApplication.class, args);
    }

}
