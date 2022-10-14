package com.example.emag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class EmagApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmagApplication.class, args);
	}
}
