package com.markdev.inventoryManagmentsSystem.security;

import com.markdev.inventoryManagmentsSystem.exceptions.CustomAccessDeniedHandler;
import com.markdev.inventoryManagmentsSystem.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilter {

    private final AuthFilter authFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception->exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(request->request
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/product-image/**").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager( );
    }

/*
*   🧑 Client (Front-end, Postman...)
                      |
                      v
              🔐 Requête HTTP (ex: GET /api/products)
                      |
                      v
              🔍 AuthFilter (JWT Filter)
             [lit le token, vérifie, injecte User]
                      |
                      v
       Spring Security vérifie les permissions
                      |
        ┌─────────────┴─────────────┐
        │                           │
  ✅ Authentifié              ❌ Pas d'accès
        │                           │
        v                           v
 Controller               CustomAccessDeniedHandler
 ou Service            ou CustomAuthenticationEntryPoint
* */

}
