package ar.edu.davinci.PetSit.controller.web.Adopcion;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/petsit/adopciones")   // ← CORREGIDO: tenía /adopciones sin /petsit
public class AdopcionController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(AdopcionController.class);

    @Autowired private AdopcionService adopcionService;
    @Autowired private MascotaService  mascotaService;
    @Autowired private UsuarioService  usuarioService;

    // ── Listado de todas las adopciones activas (vista pública logueada) ──────
    @GetMapping("/list")
    public String listAdopciones(Model model, Principal principal) throws BusinessException {
        LOGGER.info("GET /petsit/adopciones/list");
        Pageable pageable = PageRequest.of(0, 50);
        Page<Adopcion> adopciones = adopcionService.list(pageable);
        model.addAttribute("listAdopciones", adopciones.getContent());

        if (principal != null) {
            Usuario usuario = usuarioService.findByCorreo(principal.getName());
            model.addAttribute("usuario", usuario);
        }
        return "adopciones/list_adopciones";
    }

    // ── Formulario nueva adopcion ─────────────────────────────────────────────
    @GetMapping("/new")
    public String newAdopcionForm(Model model, Principal principal) throws BusinessException {
        LOGGER.info("GET /petsit/adopciones/new");
        if (principal == null) return "redirect:/petsit/home/login";

        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        List<Mascota> misMascotas = mascotaService.findByDueno(usuario);

        model.addAttribute("adopcion", new Adopcion());
        model.addAttribute("misMascotas", misMascotas);
        model.addAttribute("usuario", usuario);
        return "adopciones/new_adopcion";
    }

    // ── Guardar adopción ──────────────────────────────────────────────────────
    @PostMapping("/save")
    public String saveAdopcion(
            @ModelAttribute("adopcion") Adopcion adopcion,
            @RequestParam(value = "mascota.id", required = false) Long mascotaId,
            Principal principal) {

        LOGGER.info("POST /petsit/adopciones/save");
        try {
            if (principal != null) {
                Usuario usuario = usuarioService.findByCorreo(principal.getName());
                adopcion.setUsuario(usuario);
            }
            if (mascotaId != null) {
                Mascota mascota = mascotaService.findById(mascotaId);
                adopcion.setMascota(mascota);
            }
            if (adopcion.getEstado() == null || adopcion.getEstado().isBlank()) {
                adopcion.setEstado("ACTIVA");
            }
            if (adopcion.getId() == null) {
                adopcionService.save(adopcion);
            } else {
                adopcionService.update(adopcion);
            }
        } catch (BusinessException e) {
            LOGGER.error("Error guardando adopción: {}", e.getMessage());
        }
        return "redirect:/petsit/adopciones/list";
    }

    // ── Editar ────────────────────────────────────────────────────────────────
    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editAdopcionForm(@PathVariable("id") Long id) throws BusinessException {
        ModelAndView mav = new ModelAndView("adopciones/edit_adopcion");
        try {
            Adopcion adopcion = adopcionService.findById(id);
            mav.addObject("adopcion", adopcion);
            mav.addObject("listMascotas", mascotaService.list());
        } catch (BusinessException e) {
            LOGGER.error("Adopción no encontrada: {}", e.getMessage());
        }
        return mav;
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id:\\d+}")
    public String deleteAdopcion(@PathVariable("id") Long id) {
        adopcionService.delete(id);
        return "redirect:/petsit/adopciones/list";
    }
}
