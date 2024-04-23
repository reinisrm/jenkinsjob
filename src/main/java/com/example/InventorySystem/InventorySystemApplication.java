package com.example.InventorySystem;

import com.example.InventorySystem.models.*;
import com.example.InventorySystem.repos.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;

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
	//@Bean //Calls function when system runs
	public CommandLineRunner testModel(
			AuthorityRepo authorityRepo,
			UserRepo userRepo,
			InventoryRepo inventoryRepo,
			PersonRepo personRepo,
			LendingRepo lendingRepo

	) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {

				//Setting roles to users
				User user1 = new User("Vairis", passwordEncoderSimple().encode("123"));
				userRepo.save(user1);
				User user2 = new User("Reinis", passwordEncoderSimple().encode("321"));
				userRepo.save(user2);

				Authority auth1 = new Authority("ADMIN");
				//Authority auth2 = new Authority("USER"); Will be used in the future

				auth1.addUser(user1);
				auth1.addUser(user2);
				authorityRepo.save(auth1);

				user1.addAuthority(auth1);
				user2.addAuthority(auth1);
				userRepo.save(user1);
				userRepo.save(user2);

				//Inventory
				Inventory inv1 = new Inventory("Device1", "AD2343", "C405", "Skapis1");
				Inventory inv2 = new Inventory("Device2", "GD11", "C405", "Skapis2");
				inventoryRepo.save(inv1);
				inventoryRepo.save(inv2);

				//Person
				Person pers1 = new Person("Janis", "Ozols", "+37129774394", "2PS");
				Person pers2 = new Person("Didzis", "Lapa", "+37125554320", "1PS");
				personRepo.save(pers1);
				personRepo.save(pers2);

				//Lending
				Lending lend1 = new Lending(LocalDate.of(2023, 11, 12), inv1, pers1, pers2, LocalDate.of(2023, 12, 17), true, false, "Komentars");
				Lending lend2 = new Lending(LocalDate.of(2023, 10, 12), inv1, pers2, pers2, LocalDate.of(2023, 11, 12), true, true, "Komentars");
				lendingRepo.save(lend1);
				lendingRepo.save(lend2);
			}
		};


	}

}
