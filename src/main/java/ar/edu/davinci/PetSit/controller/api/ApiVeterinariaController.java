package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.dto.VeterinariaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/** Público (sin login), muestra lsa veterinarias ya aprobadas (listAprobadas()). */
@RestController
@RequestMapping("/petsit/api/veterinarias")
public class ApiVeterinariaController {

    @Autowired
    private VeterinariaService veterinariaService;

    @GetMapping
    public ResponseEntity<List<VeterinariaDTO>> list() {
        List<VeterinariaDTO> veterinarias = veterinariaService.listAprobadas().stream()
                .map(VeterinariaDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(veterinarias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new VeterinariaDTO(veterinariaService.findById(id)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
