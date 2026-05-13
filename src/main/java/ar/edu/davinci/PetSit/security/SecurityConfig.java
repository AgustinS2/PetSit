package ar.edu.davinci.PetSit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Panel admin solo para ADMINISTRADOR
                        .requestMatchers("/petsit/admin/**").hasRole("ADMINISTRADOR")

                        // Rutas públicas
                        .requestMatchers(
                                "/", "/petsit/", "/petsit/index",
                                "/petsit/home/login",
                                "/petsit/home/registro",
                                "/petsit/home/contacto",
                                "/petsit/home/nosotros",
                                "/petsit/home/recuperarpass",
                                "/petsit/usuarios/save",
                                "/petsit/registro/veterinaria",
                                "/petsit/registro/refugio",
                                "/petsit/registro/veterinaria/save",
                                "/petsit/registro/refugio/save",
                                "/petsit/refugios/list",
                                "/petsit/refugios/index",
                                "/petsit/veterinarias/list",
                                "/petsit/veterinarias/index",
                                "/petsit/mascotas/list",
                                // API del mapa — accesible sin login para el fetch de Leaflet
                                // (el POST de reporte acepta anónimos, Reporte.usuario es nullable)
                                "/petsit/api/mapa/**",
                                // Recursos estáticos
                                "/css/**", "/js/**", "/images/**", "/assets/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/petsit/home/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/petsit/home/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/petsit/home/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}
