package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.LoginRequestDTO;
import ar.edu.davinci.PetSit.dto.UsuarioDTO;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login usado por la app Android.
 * A diferencia del login de la web, recibe usuario y contraseña en JSON
 * y también responde en JSON, sin redirigir a una página.
 * Usa el mismo sistema de autenticación de Spring Security y, si los datos
 * son correctos, guarda al usuario en la sesión. Entonces asi la app recibe
 * una cookie JSESSIONID y puede acceder a las demás rutas protegidas igual
 * que la página web (golazo).
 *
 * Para funcionar necesita el AuthenticationManager definido como @Bean
 * en SecurityConfig (que ya esta definido).
 */
@RestController
@RequestMapping("/petsit/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        if (body.getCorreo() == null || body.getContrasena() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Authentication authRequest =
                    new UsernamePasswordAuthenticationToken(body.getCorreo(), body.getContrasena());
            Authentication authResult = authenticationManager.authenticate(authRequest);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
            // Esto es lo que deja la sesión "logueada" para los próximos
            // requests con la misma cookie JSESSIONID.
            securityContextRepository.saveContext(context, request, response);

            Usuario usuario = usuarioService.findByCorreo(body.getCorreo());
            return ResponseEntity.ok(new UsuarioDTO(usuario));
        } catch (Exception e) {
            // Credenciales inválidas o usuario inexistente -> 401 genérico
            // (no distinguimos para no filtrar qué correos existen).
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
