package ar.edu.davinci.PetSit.controller.web;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/petsit/registro")
public class RegistroPublicoController {

    private final Logger LOGGER = LoggerFactory.getLogger(RegistroPublicoController.class);


    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/uploads/";

    @Autowired private VeterinariaService veterinariaService;
    @Autowired private RefugioService     refugioService;

    // ── VETERINARIA ───────────────────────────────────────────
    @GetMapping("/veterinaria")
    public String formVeterinaria(Model model) {
        model.addAttribute("veterinaria", new Veterinaria());
        return "home/registro_veterinaria";
    }

    @PostMapping("/veterinaria/save")
    public String saveVeterinaria(
            @ModelAttribute Veterinaria veterinaria,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile) {

        try {
            // Guardar foto si se subió
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String filename = guardarFoto(fotoFile, "vet_");
                veterinaria.setFoto(filename);
            }
            // Estado PENDIENTE: el admin la aprueba antes de que sea visible
            veterinaria.setEstadoAprobacion("PENDIENTE");
            veterinaria.setActiva(false);
            veterinariaService.save(veterinaria);
            return "redirect:/petsit/registro/veterinaria/gracias";
        } catch (BusinessException | IOException e) {
            LOGGER.error("Error guardando veterinaria: {}", e.getMessage());
            return "redirect:/petsit/registro/veterinaria?error=true";
        }
    }

    @GetMapping("/veterinaria/gracias")
    public String graciasVeterinaria() {
        return "home/registro_gracias";
    }

    // ── REFUGIO ───────────────────────────────────────────────
    @GetMapping("/refugio")
    public String formRefugio(Model model) {
        model.addAttribute("refugio", new Refugio());
        return "home/registro_refugio";
    }

    @PostMapping("/refugio/save")
    public String saveRefugio(
            @ModelAttribute Refugio refugio,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile) {

        try {
            if (fotoFile != null && !fotoFile.isEmpty()) {
                String filename = guardarFoto(fotoFile, "ref_");
                refugio.setFoto(filename);
            }
            // Estado PENDIENTE: el admin lo aprueba antes de que sea visible
            refugio.setEstadoAprobacion("PENDIENTE");
            refugioService.save(refugio);
            return "redirect:/petsit/registro/refugio/gracias";
        } catch (BusinessException | IOException e) {
            LOGGER.error("Error guardando refugio: {}", e.getMessage());
            return "redirect:/petsit/registro/refugio?error=true";
        }
    }

    @GetMapping("/refugio/gracias")
    public String graciasRefugio() {
        return "home/registro_gracias";
    }

    // ── Helper: guardar foto ──────────────────────────────────
    private String guardarFoto(MultipartFile file, String prefix) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : ".jpg";
        String filename = prefix + System.currentTimeMillis() + extension;

        Path dest = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return "uploads/" + filename;
    }
}
