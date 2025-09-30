package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;

@SuppressWarnings("unused")
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(GET,"/login", "/rooms", "/api/rooms").permitAll()
                        .requestMatchers("/createRoom", "/editRoom", "/actuator/**").hasRole("ADMIN")
                        .requestMatchers(POST, "/api/rooms").hasRole("ADMIN")
                        .requestMatchers(PUT, "/api/rooms/{id}").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/rooms/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .defaultSuccessUrl("/rooms", true)
                        .permitAll());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
