package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}
    */


    //@Bean
    public UserDetailsManagerImpl userDetailsManager() {
        UserDetailsManagerImpl manager = new UserDetailsManagerImpl();
        return manager;
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
                        .requestMatchers("/lending").hasAnyAuthority("ADMIN") // retrieve
                        .requestMatchers("/lending/**").hasAnyAuthority("ADMIN") // retrieve one
                        .requestMatchers("/lending/delete/**").hasAnyAuthority("ADMIN") // delete
                        .requestMatchers("/lending/create").hasAnyAuthority("ADMIN") // create
                        .requestMatchers("/lending/update/**").hasAnyAuthority("ADMIN") // update
                        .requestMatchers("/person").hasAnyAuthority("ADMIN")
                        .requestMatchers("/person/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/person/delete/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/person/create").hasAnyAuthority("ADMIN")
                        .requestMatchers("/person/update/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/inventory").hasAnyAuthority("ADMIN")
                        .requestMatchers("/inventory/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/inventory/delete/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/inventory/create").hasAnyAuthority("ADMIN")
                        .requestMatchers("/inventory/update/**").hasAnyAuthority("ADMIN"))
                        .formLogin(login -> login
                            .defaultSuccessUrl("/lending")  // Redirect after successful login
                            .permitAll())
                        .logout(logout -> logout
                             .permitAll());







        return http.build();
    }









}

