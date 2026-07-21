package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.MascotaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
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
 * Estos endpoints quedan detrás de sesión (anyRequest().authenticated() en
 * SecurityConfig, ya que no los agregamos a la lista permitAll).
 */
@RestController
@RequestMapping("/petsit/api/mascotas")
public class ApiMascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    /** Es lo mismo que a /petsit/mascotas/mis_mascotas pero en JSON. */
    @GetMapping("/mias")
    public ResponseEntity<?> misMascotas(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            List<MascotaDTO> mascotas = mascotaService.findByDueno(usuario).stream()
                    .map(MascotaDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(mascotas);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMascota(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new MascotaDTO(mascotaService.findById(id)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /** Crea una mascota para el usuario logueado */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Mascota mascota, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario duenoActual = usuarioService.findByCorreo(principal.getName());
            mascota.setId(null);
            mascota.setDueno(duenoActual);
            Mascota guardada = mascotaService.save(mascota);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MascotaDTO(guardada));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
