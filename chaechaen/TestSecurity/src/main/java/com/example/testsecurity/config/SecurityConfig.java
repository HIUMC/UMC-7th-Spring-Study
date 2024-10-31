package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티에 의해 관리됨
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {

        return RoleHierarchyImpl.fromHierarchy("""
            ROLE_C > ROLE_B
            ROLE_B > ROLE_A
            """);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 특정 경로로 요청이 왔을 때 그 경로는 모든 사용자에게 오픈시켜주고,
        // 특정 admin 경로는 admin 사용자에게만 오픈시켜주고,
        // 다른 경로는 로그인을 진행해야 접근할 수 있다.

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/").hasAnyRole("A")
                        .requestMatchers("/manager").hasAnyRole("B")
                        .requestMatchers("/admin").hasAnyRole("C")
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        // formLogin 방식
//        http
//                .formLogin((auth) -> auth.loginPage("/login") // 우리가 설정해둔 로그인 페이지 경로가 어디있는지 설정해줌
//                    .loginProcessingUrl("/loginProc")
//                    .permitAll()
//                );

        // httpBasic 방식
        http
                .httpBasic(Customizer.withDefaults());

        http
                .csrf((auth) -> auth.disable());

//        http
//                .sessionManagement((auth) -> auth
//                        .maximumSessions(1) // 하나의 아이디를 통한 동시 접속 중복 로그인을 허용하는 개수
//                        .maxSessionsPreventsLogin(true)); // 다중 로그인 개수를 초과하였을 경우 처리 방법
//
//        http
//                .sessionManagement((auth) -> auth
//                        .sessionFixation().changeSessionId());

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
