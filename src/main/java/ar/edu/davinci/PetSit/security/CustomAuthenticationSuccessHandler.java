package ar.edu.davinci.PetSit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Redirige al usuario al panel correcto según su rol después del login.
 *
 *   ROLE_ADMINISTRADOR  →  /petsit/admin/index
 *   ROLE_DUENO          →  /petsit/usuarios/index
 *   ROLE_VETERINARIO    →  /petsit/usuarios/index
 *   ROLE_REFUGIO        →  /petsit/usuarios/index
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = "/petsit/usuarios/index"; // default

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMINISTRADOR")) {
                redirectUrl = "/petsit/admin/index";
                break;
            }
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
