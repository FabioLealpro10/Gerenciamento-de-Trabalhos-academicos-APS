package com.gerenciador.trabalhos.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gerenciador.trabalhos.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

                return configuration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http.cors(cors -> {})
                        .csrf(csrf -> csrf.disable())

                        .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                        .authorizeHttpRequests(auth -> auth

                                        // LIBERA OPTIONS (CORS)
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                        // AUTH
                                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/auth/esqueci-senha").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/auth/verificar-codigo").permitAll()

                                        // APIs
                                        .requestMatchers(HttpMethod.POST, "/api/alunos/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/professores/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/disciplinas/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/trabalhos/**").permitAll()

                                        .requestMatchers(
                                                        HttpMethod.GET,
                                                        "/disciplinas/**",
                                                        "/api/disciplinas/**",
                                                        "/trabalhos/**",
                                                        "/entregas/**")
                                        .permitAll()

                                        // Swagger
                                        .requestMatchers(
                                                        "/swagger-ui.html",
                                                        "/swagger-ui/**",
                                                        "/v3/api-docs/**",
                                                        "/h2-console/**")
                                        .permitAll()

                                        .anyRequest().authenticated())

                        .addFilterBefore(
                                        jwtAuthenticationFilter,
                                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
}


        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5501",
                "http://localhost:5501",
                "http://localhost:4200"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
        }
}