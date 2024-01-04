package com.myown.app.sample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {

        http.authorizeExchange(authExchange -> authExchange.pathMatchers("/","/home","/actuator","/actuator/**","/images/**").permitAll().anyExchange().authenticated()).csrf(customizer -> customizer.disable());

        return http.build();

    }

}
