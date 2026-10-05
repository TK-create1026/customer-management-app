package com.tkcreate.customer_management_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

    // パスワードをBCryptでハッシュ化するための設定
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Spring Securityの設定
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{

        // 開発中のためCSRF対策を無効化
        http.csrf(csrf -> csrf.disable())

        // 全てのURLへアクセスを許可
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        
        return http.build();
    }
}