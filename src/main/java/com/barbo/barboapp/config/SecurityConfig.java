package com.barbo.barboapp.config;

import com.barbo.barboapp.util.JwtFilter;
import com.barbo.barboapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ✅ Public
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/barbers/register").permitAll()

                        // ✅ USER - own profile only
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/email/**").hasRole("ADMIN")

                        // ✅ Barbers - USER can only VIEW
                        .requestMatchers(HttpMethod.GET, "/api/barbers/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET,"/api/barbers/id/**").hasAnyRole("ADMIN","BARBER")
                        .requestMatchers(HttpMethod.POST, "/api/barbers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbers/id/**").hasAnyRole("ADMIN","BARBER")
                        .requestMatchers(HttpMethod.PATCH, "/api/barbers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbers/**").hasRole("ADMIN")

                        // ✅ Services
                        .requestMatchers(HttpMethod.GET, "/api/services/**").hasAnyRole("ADMIN", "USER","BARBER")
                        .requestMatchers(HttpMethod.POST, "/api/services/**").hasAnyRole("ADMIN","BARBER")
                        .requestMatchers(HttpMethod.PUT, "/api/services/**").hasAnyRole("ADMIN","BARBER")
                        .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasAnyRole("ADMIN","BARBER")

                        // ✅ Appointments - USER can view own, book, cancel only
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/cancel").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/complete").hasAnyRole("ADMIN","BARBER")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}