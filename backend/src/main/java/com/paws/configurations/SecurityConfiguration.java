package com.paws.configurations;

import com.paws.security.CustomerUserDetailsServiceImpl;
import com.paws.security.EmployeeUserDetailsServiceImpl;
import com.paws.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final CustomerUserDetailsServiceImpl customerUserDetailsServiceImpl;
    private final EmployeeUserDetailsServiceImpl employeeUserDetailsServiceImpl;

    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter, CustomerUserDetailsServiceImpl customerUserDetailsServiceImpl, EmployeeUserDetailsServiceImpl employeeUserDetailsServiceImpl) {
        this.jwtFilter = jwtFilter;
        this.customerUserDetailsServiceImpl = customerUserDetailsServiceImpl;
        this.employeeUserDetailsServiceImpl = employeeUserDetailsServiceImpl;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(employeeAuthProvider())
                .authorizeHttpRequests(x ->
                        x.requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/error", "/auth/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(x -> x
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/", true).permitAll()
                        .failureUrl("/auth/login?error=true").permitAll())
                .logout(x -> x
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                        .logoutSuccessUrl("/auth/login")
                        .permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager customerAuthManager() {
        return new ProviderManager(customerAuthProvider());
    }

    private DaoAuthenticationProvider customerAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    private DaoAuthenticationProvider employeeAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(employeeUserDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
