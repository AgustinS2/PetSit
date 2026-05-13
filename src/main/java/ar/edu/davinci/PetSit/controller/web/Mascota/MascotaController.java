package ar.edu.davinci.PetSit.controller.web.Mascota;

import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
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
@RequestMapping("/petsit/mascotas")
public class MascotaController {

    private final Logger LOGGER = LoggerFactory.getLogger(MascotaController.class);

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private UsuarioService usuarioService;

    // ──────────────────────────────────────────────
    //  LISTADO PÚBLICO (todas las mascotas, paginado)
    // ──────────────────────────────────────────────
    @GetMapping("/list")
    public String listMascotas(Model model) {
        LOGGER.info("GET /petsit/mascotas/list");
        Pageable pageable = PageRequest.of(0, 20);
        Page<Mascota> mascotas = mascotaService.list(pageable);
        model.addAttribute("listMascotas", mascotas.getContent());
        model.addAttribute("pageNumber", mascotas.getPageable().getPageNumber());
        model.addAttribute("totalPages", mascotas.getTotalPages());
        return "mascotas/list_mascotas";
    }

    // ──────────────────────────────────────────────
    //  MIS MASCOTAS (solo las del usuario logueado)
    // ──────────────────────────────────────────────
    @GetMapping("/mis_mascotas")
    public String misMascotas(Model model, Principal principal) throws BusinessException {
        LOGGER.info("GET /petsit/mascotas/mis_mascotas — usuario: {}", principal.getName());

        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        List<Mascota> mascotas = mascotaService.findByDueno(usuario);

        model.addAttribute("usuario", usuario);          // para el navbar (th:text="${usuario.nombre}")
        model.addAttribute("listMascotas", mascotas);    // para iterar en el template
        return "mascotas/mis_mascotas";
    }

    // ──────────────────────────────────────────────
    //  NUEVA MASCOTA
    // ──────────────────────────────────────────────
    @GetMapping("/new")
    public String newMascotaForm(Model model) {
        LOGGER.info("GET /petsit/mascotas/new");
        model.addAttribute("mascota", new Mascota());
        // Lista de usuarios para el <select> del dueño (sólo vista de admin)
        model.addAttribute("usuarios", usuarioService.list());
        return "mascotas/new_mascotas";   // ← nombre correcto del template
    }

    // ──────────────────────────────────────────────
    //  GUARDAR (crear o actualizar)
    //
    //  El truco clave: Spring NO puede convertir
    //  automáticamente un <select name="dueno.id">
    //  en un objeto Usuario completo sin un Converter.
    //  Recibimos el ID por separado y lo resolvemos
    //  manualmente aquí.
    // ──────────────────────────────────────────────
    @PostMapping("/save")
    public String saveMascota(
            @ModelAttribute("mascota") Mascota mascota,
            @RequestParam(value = "dueno.id", required = false) Long duenoId,
            Principal principal) {

        LOGGER.info("POST /petsit/mascotas/save — id mascota: {}", mascota.getId());

        try {
            // Resolver el dueño:
            // Si viene un ID desde el <select> (panel admin) lo usamos.
            // Si no, el dueño es el usuario logueado (alta desde "Mis mascotas").
            if (duenoId != null) {
                Usuario dueno = usuarioService.findById(duenoId);
                mascota.setDueno(dueno);
            } else if (mascota.getDueno() == null && principal != null) {
                Usuario dueno = usuarioService.findByCorreo(principal.getName());
                mascota.setDueno(dueno);
            }

            if (mascota.getId() == null) {
                mascotaService.save(mascota);
            } else {
                mascotaService.update(mascota);
            }
        } catch (BusinessException e) {
            LOGGER.error("Error guardando mascota: {}", e.getMessage());
        }

        // Redirige a "mis mascotas" si hay sesión, si no al listado general
        if (principal != null) {
            return "redirect:/petsit/mascotas/mis_mascotas";
        }
        return "redirect:/petsit/mascotas/list";
    }

    // ──────────────────────────────────────────────
    //  EDITAR
    // ──────────────────────────────────────────────
    @GetMapping("/edit/{id:\\d+}")
    public ModelAndView editMascotaForm(@PathVariable("id") Long id) {
        LOGGER.info("GET /petsit/mascotas/edit/{}", id);
        ModelAndView mav = new ModelAndView("mascotas/edit_mascotas");   // ← nombre correcto
        try {
            Mascota mascota = mascotaService.findById(id);
            mav.addObject("mascota", mascota);
            mav.addObject("usuarios", usuarioService.list()); // para el <select> del dueño
        } catch (BusinessException e) {
            LOGGER.error("Mascota no encontrada: {}", e.getMessage());
        }
        return mav;
    }

    // ──────────────────────────────────────────────
    //  ELIMINAR
    // ──────────────────────────────────────────────
    @GetMapping("/delete/{id:\\d+}")
    public String deleteMascota(@PathVariable("id") Long id, Principal principal) {
        LOGGER.info("GET /petsit/mascotas/delete/{}", id);
        mascotaService.delete(id);
        if (principal != null) {
            return "redirect:/petsit/mascotas/mis_mascotas";
        }
        return "redirect:/petsit/mascotas/list";
    }

    // ──────────────────────────────────────────────
    //  Páginas estáticas (adoptar, document)
    // ──────────────────────────────────────────────
    @GetMapping("/adoptar")
    public String adoptar() {
        return "mascotas/adoptar_mascotas";
    }

    @GetMapping("/document")
    public String document() {
        return "mascotas/document_mascotas";
    }
}
