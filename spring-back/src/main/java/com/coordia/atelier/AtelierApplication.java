package com.coordia.atelier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtelierApplication {

	public static void main(String[] args) {
		System.out.println("[START] API is running...");
		SpringApplication.run(AtelierApplication.class, args);
	}

}
