package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.CrearPostulacionRequestDTO;
import ar.edu.davinci.PetSit.dto.PostulacionDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Todo detrás de sesión (no está en permitAll de SecurityConfig, así que lo
 * cubre anyRequest().authenticated()).
 */
@RestController
@RequestMapping("/petsit/api/postulaciones")
public class ApiPostulacionController {

    @Autowired
    private PostulacionService postulacionService;

    @Autowired
    private AdopcionService adopcionService;

    @Autowired
    private UsuarioService usuarioService;

    /** Postulaciones que hice yo a adopciones de otras personas. */
    @GetMapping("/mias")
    public ResponseEntity<?> mias(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            return ResponseEntity.ok(aDTOs(postulacionService.findByUsuario(usuario)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /** Postulaciones que recibieron las adopciones que publiqué yo. */
    @GetMapping("/recibidas")
    public ResponseEntity<?> recibidas(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            return ResponseEntity.ok(aDTOs(postulacionService.findRecibidas(usuario)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /** Postularse a una adopción activa de otra persona (una sola vez). */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearPostulacionRequestDTO request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request.getAdopcionId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta adopcionId.");
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            Adopcion adopcion = adopcionService.findById(request.getAdopcionId());

            if (!"ACTIVA".equals(adopcion.getEstado())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La adopción ya no está activa.");
            }
            if (adopcion.getUsuario() != null && adopcion.getUsuario().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No podés postularte a tu propia adopción.");
            }
            if (postulacionService.yaPostulado(adopcion, usuario)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya te postulaste a esta adopción.");
            }

            Postulacion postulacion = Postulacion.builder()
                    .adopcion(adopcion)
                    .usuario(usuario)
                    .mensaje(request.getMensaje())
                    .build();

            Postulacion guardada = postulacionService.save(postulacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(new PostulacionDTO(guardada));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private List<PostulacionDTO> aDTOs(List<Postulacion> postulaciones) {
        return postulaciones.stream().map(PostulacionDTO::new).collect(Collectors.toList());
    }
}
