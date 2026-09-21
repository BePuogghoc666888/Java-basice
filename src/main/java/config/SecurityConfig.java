package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/h2-console/**", "/home", "/login", "/register").permitAll()
                .requestMatchers("/dashboard").hasAuthority("ROLE_AD")
                .requestMatchers("/profile").hasAnyAuthority("ROLE_STAFF", "ROLE_KH")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/api/auth/login") // Endpoint xử lý login
                .successHandler((request, response, authentication) -> {
                    // Tự động chuyển hướng theo Role sau khi đăng nhập thành công
                    var authorities = authentication.getAuthorities();
                    String redirectUrl = "/home";

                    for (var auth : authorities) {
                        if (auth.getAuthority().equals("ROLE_AD")) {
                            redirectUrl = "/dashboard";
                            break;
                        } else if (auth.getAuthority().equals("ROLE_STAFF")) {
                            redirectUrl = "/profile";
                            break;
                        }
                    }
                    response.sendRedirect(redirectUrl);
                })
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/home").permitAll())
            .headers(headers -> headers.frameOptions().disable()); // Enable cho H2 Console

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}