package com.yht.exerciseassist.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.JwtTokenResolver;
import com.yht.exerciseassist.jwt.filter.ExceptionHandlerFilter;
import com.yht.exerciseassist.jwt.filter.JwtAuthenticationFilter;
import com.yht.exerciseassist.jwt.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final JwtTokenResolver jwtTokenResolver;
    private final JWTService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/email").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/signin").permitAll()
                        .requestMatchers("/find/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/diary/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/diary").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/media/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/post/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/accuse/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/comment/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/chat/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtTokenResolver), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(objectMapper, jwtService), JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
