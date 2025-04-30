package com.example.inventorysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.InventorySystem")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class InventorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorySystemApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoderSimple() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}