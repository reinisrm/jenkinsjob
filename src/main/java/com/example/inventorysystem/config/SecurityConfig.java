package com.example.inventorysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //@Bean
    public UserDetailsManagerImpl userDetailsManager() {
        return new UserDetailsManagerImpl();
    }

    @Bean
    PasswordEncoder passwordEncoderSimple2() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);


        authenticationManagerBuilder.
                userDetailsService(userDetailsManager()).passwordEncoder(passwordEncoderSimple2());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/challenges/").permitAll()
                        .requestMatchers("/challenges/{postId}").permitAll()
                        .requestMatchers("/challenges/create").permitAll()
                        .requestMatchers("/challenges/update/**").permitAll()
                        .requestMatchers("/challenges/delete/**").permitAll()
                        .requestMatchers("/lending").permitAll()
                        .requestMatchers("/lending/**").permitAll()
                        .requestMatchers("/lending/delete/**").permitAll()
                        .requestMatchers("/lending/create").permitAll()
                        .requestMatchers("/lending/update/**").permitAll()
                        .requestMatchers("/person").permitAll()
                        .requestMatchers("/person/**").permitAll()
                        .requestMatchers("/person/delete/**").permitAll()
                        .requestMatchers("/person/create").permitAll()
                        .requestMatchers("/person/update/**").permitAll()
                        .requestMatchers("/inventory").permitAll()
                        .requestMatchers("/inventory/**").permitAll()
                        .requestMatchers("/inventory/delete/**").permitAll()
                        .requestMatchers("/inventory/create").permitAll()
                        .requestMatchers("/inventory/update/**").permitAll())
                        .formLogin(login -> login
                            .defaultSuccessUrl("/challenges/")  // Redirect after successful login
                            .permitAll())
                        .logout(logout -> logout
                             .permitAll());

        return http.build();
    }
}