package com.codewithdurgesh.blog.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codewithdurgesh.blog.security.CustomUserDetailService;
import com.codewithdurgesh.blog.security.JwtAccessDeniedHandler;
import com.codewithdurgesh.blog.security.JwtAuthenticationEntryPoint;
import com.codewithdurgesh.blog.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            CustomUserDetailService userDetailService,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAccessDeniedHandler accessDeniedHandler
    ) {
        this.userDetailService = userDetailService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    // PASSWORD
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AUTH PROVIDER
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AUTH MANAGER
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ⭐⭐⭐ FINAL CORS FIX
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:8081",
                "http://localhost:3000",
                "http://localhost:4200",
                "https://blog-app-backend-production-f8e2.up.railway.app"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // MAIN SECURITY
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth

                    // AUTH APIs
                    .requestMatchers("/api/v1/auth/**").permitAll()

                    // SWAGGER
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll()

                    // PUBLIC GET APIs
                    .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/post/image/**").permitAll()

                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}