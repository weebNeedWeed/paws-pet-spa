package com.paws.presentation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, CustomerUserDetailsService customerUserDetailsService, EmployeeUserDetailsService employeeUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customerUserDetailsService = customerUserDetailsService;
        this.employeeUserDetailsService = employeeUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(x -> x.disable())
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(x -> x
                        .requestMatchers("/api/customers/login", "/api/customers/register").permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain dashboardFilterChain(HttpSecurity http) throws  Exception {
        http
                .securityMatcher("/**")
                .csrf(x -> x.disable())
                .authenticationProvider(employeeAuthProvider())
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/error", "/auth/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(x -> x
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .loginPage("/auth/login")
                        .failureUrl("/auth/login?error")
                        .permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager customerAuthManager() {
        return new ProviderManager(customerAuthProvider());
    }

    private DaoAuthenticationProvider customerAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    private DaoAuthenticationProvider employeeAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(employeeUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
