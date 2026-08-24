package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.AdopcionDTO;
import ar.edu.davinci.PetSit.dto.CrearAdopcionRequestDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GET son públicos (permitAll en SecurityConfig, solo para método GET).
 * POST queda detrás de sesión (anyRequest().authenticated()).
 */
@RestController
@RequestMapping("/petsit/api/adopciones")
public class ApiAdopcionController {

    @Autowired
    private AdopcionService adopcionService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    /** Listado público: solo adopciones activas. */
    @GetMapping
    public ResponseEntity<List<AdopcionDTO>> list() {
        List<AdopcionDTO> adopciones = adopcionService.listActivas().stream()
                .map(AdopcionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adopciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new AdopcionDTO(adopcionService.findById(id)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /** Publica una mascota propia en adopción. La mascota tiene que ser del usuario logueado. */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearAdopcionRequestDTO request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (request.getMascotaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta mascotaId.");
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            Mascota mascota = mascotaService.findById(request.getMascotaId());

            if (mascota.getDueno() == null || !mascota.getDueno().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Esa mascota no te pertenece.");
            }

            Adopcion adopcion = Adopcion.builder()
                    .mascota(mascota)
                    .usuario(usuario)
                    .descripcion(request.getDescripcion())
                    .ubicacion(request.getUbicacion())
                    .foto(request.getFoto())
                    .build();

            Adopcion guardada = adopcionService.save(adopcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AdopcionDTO(guardada));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
