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
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/petsit/api/mapa/**")
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
