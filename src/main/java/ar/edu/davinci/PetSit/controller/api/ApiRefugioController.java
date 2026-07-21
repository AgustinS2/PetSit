package ar.edu.davinci.PetSit.controller.api;

import ar.edu.davinci.PetSit.dto.RefugioDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/** Público (sin login): solo expone refugios ya aprobados (listAprobadas()). */
@RestController
@RequestMapping("/petsit/api/refugios")
public class ApiRefugioController {

    @Autowired
    private RefugioService refugioService;

    @GetMapping
    public ResponseEntity<List<RefugioDTO>> list() {
        List<RefugioDTO> refugios = refugioService.listAprobadas().stream()
                .map(RefugioDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(refugios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RefugioDTO(refugioService.findById(id)));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
