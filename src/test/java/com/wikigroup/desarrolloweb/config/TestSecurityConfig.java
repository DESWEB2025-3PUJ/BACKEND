package com.wikigroup.desarrolloweb.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para tests que deshabilita completamente la autenticación.
 * 
 * Esta configuración:
 * - Se activa SOLO en el perfil "test"
 * - Permite todas las peticiones sin autenticación
 * - Usa H2 en memoria (configurado en application-test.properties)
 * - No requiere JWT ni ningún tipo de autenticación
 * - Proporciona un PasswordEncoder para beans que lo necesiten
 */
@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Deshabilitar frames para H2 console si se necesita
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            );
        
        return http.build();
    }
    
    /**
     * PasswordEncoder para tests - necesario para AuthService y otros componentes
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * UserDetailsService simple para tests
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("testuser")
                .password(passwordEncoder().encode("testpass"))
                .roles("USER")
                .build()
        );
    }
    
    /**
     * AuthenticationManager para tests - necesario para AuthService
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
