package ar.edu.davinci.PetSit.controller.web;

import ar.edu.davinci.PetSit.controller.PetSitApp;
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

/**
 * Rutas públicas (sin login) para que veterinarias y refugios
 * completen su ficha y queden visibles en la plataforma.
 * El admin luego puede editarlas / activarlas desde el panel.
 */
@Controller
@RequestMapping("/petsit/registro")
public class RegistroPublicoController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(RegistroPublicoController.class);

    @Autowired private VeterinariaService veterinariaService;
    @Autowired private RefugioService     refugioService;

    // ── Formulario: registrar veterinaria ─────────────────────────────────────
    @GetMapping("/veterinaria")
    public String formVeterinaria(Model model) {
        model.addAttribute("veterinaria", new Veterinaria());
        return "home/registro_veterinaria";
    }

    @PostMapping("/veterinaria/save")
    public String saveVeterinaria(@ModelAttribute Veterinaria veterinaria) {
        try {
            // Las nuevas veterinarias arrancan inactivas; el admin las activa
            veterinaria.setActiva(false);
            veterinariaService.save(veterinaria);
            LOGGER.info("Nueva veterinaria registrada: {}", veterinaria.getNombre());
        } catch (BusinessException e) {
            LOGGER.error("Error registrando veterinaria: {}", e.getMessage());
        }
        return "redirect:/petsit/home/nosotros?registrado=veterinaria";
    }

    // ── Formulario: registrar refugio ─────────────────────────────────────────
    @GetMapping("/refugio")
    public String formRefugio(Model model) {
        model.addAttribute("refugio", new Refugio());
        return "home/registro_refugio";
    }

    @PostMapping("/refugio/save")
    public String saveRefugio(@ModelAttribute Refugio refugio) {
        try {
            refugioService.save(refugio);
            LOGGER.info("Nuevo refugio registrado: {}", refugio.getNombre());
        } catch (BusinessException e) {
            LOGGER.error("Error registrando refugio: {}", e.getMessage());
        }
        return "redirect:/petsit/home/nosotros?registrado=refugio";
    }
}
