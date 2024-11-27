package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.hoidanit.jobhunter.service.UserDetailCustom;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // chúng ta sử dụng thuật toán mã hóa là bcrypt
        return new BCryptPasswordEncoder();
    }

    // cái này dùng để khi người dùng chưa đăng nhập vẫn có thể vô trang, ví dụ như
    // trang chủ chẳng hạn
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/").permitAll()
                                .anyRequest().permitAll())
                .formLogin(f -> f.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // cách cũ Spring MVC
    // @Bean
    // public DaoAuthenticationProvider authProvider(PasswordEncoder
    // passwordEncoder, UserDetailCustom userDetailCustom){
    // DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    // authProvider.setPasswordEncoder(passwordEncoder);
    // authProvider.setUserDetailsService(userDetailCustom);
    // return authProvider;
    // }

}