package com.kh.youtube.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 스프링에서는 security-context.xml에 설정

        http.authorizeHttpRequests(); // http 시큐리티
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll() // xml에 pattern 부분(hasanyroll, hasroll 등)
                .anyRequest().authenticated(); // 그 외의 것(authenticated: 인증 되었을 때만 처리하겠다)

        // JWT 토큰 생성부터 필터처리까지 전부 세팅해놓음
        // JWT 필터 등록을 여기서 해줘야 함
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);


        return http.build();
    }

}
