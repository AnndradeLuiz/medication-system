package com.luiz.medication_system.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityFilter securityFilter;
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Libera o login
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Permissões de Leitura (Todos os cargos logados)
                        .requestMatchers(HttpMethod.GET, "/medications/**").hasAnyRole("ENF", "TEC", "TRIAGEM", "ENF_GERENTE", "ADM_TI")
                        .requestMatchers(HttpMethod.GET, "/employees/**").hasAnyRole("ENF_GERENTE", "ADM_TI")

                        // Permissões de Escrita/Gestão (Apenas Gerente e TI)
                        .requestMatchers(HttpMethod.POST, "/employees/**").hasAnyRole("ENF_GERENTE", "ADM_TI")
                        .requestMatchers(HttpMethod.PUT, "/employees/**").hasAnyRole("ENF_GERENTE", "ADM_TI")
                        .requestMatchers(HttpMethod.DELETE, "/employees/**").hasAnyRole("ENF_GERENTE", "ADM_TI")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}