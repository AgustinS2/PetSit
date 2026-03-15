package ar.edu.davinci.PetSit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/index",
                    "/petsit/index",
                    "/petsit/home/login",
                    "/petsit/home/registro",
                    "/petsit/home/contacto",
                    "/petsit/home/nosotros",
                    "/petsit/home/recuperarpass",
                    "/petsit/usuarios/save",
                    "/petsit/usuarios/usuarios/save",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/assets/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/petsit/home/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/petsit/usuarios/index", true)
                .failureUrl("/petsit/home/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/petsit/home/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}