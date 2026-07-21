package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.domain.TipoUsuario;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.RegistroRequestDTO;
import ar.edu.davinci.PetSit.dto.UsuarioDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/petsit/api/usuarios")
public class ApiUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /** Usuario logueado actualmente, según la cookie de sesión enviada. */
    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            return ResponseEntity.ok(new UsuarioDTO(usuario));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Siempre crea un usuario tipo DUENO (dueño de mascota).
     * Registro de refugio/veterinaria sigue siendo por el sitio web (RegistroPublicoController), no por acá.
     * IMPORTANTE: NO ENCRIPTEMOS DOS VECES PORQUE SINO EL LOGIN VA A FALLAR
     * YA LO HACEMOS CON BCryptPasswordEncoder
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequestDTO body) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(body.getNombre());
            usuario.setApellido(body.getApellido());
            usuario.setCorreo(body.getCorreo());
            usuario.setContrasena(body.getContrasena());
            usuario.setTelefono(body.getTelefono());
            usuario.setTipo(TipoUsuario.DUENO);

            Usuario guardado = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDTO(guardado));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
