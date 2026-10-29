package cn.modernhistory.auth;

import cn.modernhistory.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
        return email ->
                users
                        .findByEmail(email)
                        .map(
                                u ->
                                        org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
                                                .password(u.getPasswordHash())
                                                .disabled(!u.isEnabled())
                                                .authorities(
                                                        u.getRoles().stream()
                                                                .map(r -> "ROLE_" + r.getCode().name())
                                                                .toArray(String[]::new))
                                                .build())
                        .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http, JwtAuthenticationFilter jwt, ObjectMapper mapper) throws Exception {
        http.csrf(c -> c.disable())
                .cors(
                        c ->
                                c.configurationSource(
                                        req -> {
                                            var cfg = new CorsConfiguration();
                                            cfg.setAllowedOriginPatterns(
                                                    List.of("http://localhost:*", "http://127.0.0.1:*"));
                                            cfg.setAllowedMethods(
                                                    List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                                            cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                                            return cfg;
                                        }))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e ->
                                e.authenticationEntryPoint(
                                                (req, res, ex) -> {
                                                    res.setStatus(401);
                                                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                                    mapper.writeValue(
                                                            res.getOutputStream(), ApiResponse.failure("UNAUTHORIZED", "请先登录"));
                                                })
                                        .accessDeniedHandler(
                                                (req, res, ex) -> {
                                                    res.setStatus(403);
                                                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                                    mapper.writeValue(
                                                            res.getOutputStream(), ApiResponse.failure("FORBIDDEN", "无权访问该资源"));
                                                }))
                .authorizeHttpRequests(
                        a ->
                                a.requestMatchers(
                                                "/api/auth/login",
                                                "/api/auth/register",
                                                "/api/health",
                                                "/api/public/**",
                                                "/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
