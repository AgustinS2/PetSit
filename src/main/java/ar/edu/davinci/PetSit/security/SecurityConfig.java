package ar.edu.davinci.PetSit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 *   1. Agregue "/petsit/api/**" a la lista de rutas exceptuadas de CSRF
 *      (porque la app Android no manda token CSRF).
 *   2. Cambie a permitAll(): login/registro de la app y los listados
 *      públicos de refugios/veterinarias vía la nueva API JSON.
 *   3. Se agrego el bean AuthenticationManager, que hacia falta para poder
 *      autenticar manualmente desde ApiAuthController y asi poder autenticar desde la app.
 *
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/petsit/api/mapa/**",
                                "/petsit/api/**"
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/petsit/admin/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                "/", "/petsit/", "/petsit/index",
                                "/petsit/home/login",
                                "/petsit/home/registro",
                                "/petsit/home/contacto",
                                "/petsit/home/nosotros",
                                "/petsit/home/recuperarpass",
                                "/petsit/usuarios/save",
                                // Registro público de vet y refugio
                                "/petsit/registro/veterinaria",
                                "/petsit/registro/veterinaria/save",
                                "/petsit/registro/veterinaria/gracias",
                                "/petsit/registro/refugio",
                                "/petsit/registro/refugio/save",
                                "/petsit/registro/refugio/gracias",
                                // Listados públicos
                                "/petsit/refugios/list",
                                "/petsit/refugios/index",
                                "/petsit/veterinarias/list",
                                "/petsit/veterinarias/index",
                                "/petsit/mascotas/list",
                                // API mapa pública
                                "/petsit/api/mapa/**",
                                // --- Nuevo: API JSON para la app Android ---
                                "/petsit/api/auth/login",
                                "/petsit/api/usuarios/registro",
                                "/petsit/api/refugios/**",
                                "/petsit/api/veterinarias/**",
                                // Recursos estáticos
                                "/css/**", "/js/**", "/images/**", "/assets/**"
                        ).permitAll()

                        // Listado y detalle de adopciones
                        .requestMatchers(HttpMethod.GET, "/petsit/api/adopciones/**").permitAll()
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
