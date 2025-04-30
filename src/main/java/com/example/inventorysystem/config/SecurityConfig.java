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
                        .requestMatchers("/challenges/").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/challenges/{postId}").hasAuthority("ADMIN")
                        .requestMatchers("/challenges/create").hasAuthority("ADMIN")
                        .requestMatchers("/challenges/update/**").hasAuthority("ADMIN")
                        .requestMatchers("/challenges/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/lending").hasAuthority("ADMIN") // retrieve
                        .requestMatchers("/lending/**").hasAuthority("ADMIN") // retrieve one
                        .requestMatchers("/lending/delete/**").hasAuthority("ADMIN") // delete
                        .requestMatchers("/lending/create").hasAuthority("ADMIN") // create
                        .requestMatchers("/lending/update/**").hasAuthority("ADMIN") // update
                        .requestMatchers("/person").hasAuthority("ADMIN")
                        .requestMatchers("/person/**").hasAuthority("ADMIN")
                        .requestMatchers("/person/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/person/create").hasAuthority("ADMIN")
                        .requestMatchers("/person/update/**").hasAuthority("ADMIN")
                        .requestMatchers("/inventory").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/inventory/**").hasAuthority("ADMIN")
                        .requestMatchers("/inventory/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/inventory/create").hasAuthority("ADMIN")
                        .requestMatchers("/inventory/update/**").hasAuthority("ADMIN"))
                        .formLogin(login -> login
                            .defaultSuccessUrl("/challenges/")  // Redirect after successful login
                            .permitAll())
                        .logout(logout -> logout
                             .permitAll());

        return http.build();
    }
}