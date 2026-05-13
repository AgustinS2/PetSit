package ar.edu.davinci.PetSit.controller.web.Postulacion;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/petsit/postulaciones")
public class PostulacionController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(PostulacionController.class);

    @Autowired private PostulacionService postulacionService;
    @Autowired private AdopcionService    adopcionService;
    @Autowired private UsuarioService     usuarioService;

    // ── Mis postulaciones ─────────────────────────────────────────────────────
    @GetMapping("/mis")
    public String misPostulaciones(Model model, Principal principal) throws BusinessException {
        if (principal == null) return "redirect:/petsit/home/login";
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        model.addAttribute("listPostulaciones", postulacionService.findByUsuario(usuario));
        model.addAttribute("usuario", usuario);
        return "postulaciones/list_postulaciones";
    }

    // ── Listado general (admin) ───────────────────────────────────────────────
    @GetMapping("/list")
    public String listPostulaciones(Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Postulacion> postulaciones = postulacionService.list(pageable);
        model.addAttribute("listPostulaciones", postulaciones.getContent());
        return "postulaciones/list_postulaciones";
    }

    // ── Nueva postulación ─────────────────────────────────────────────────────
    @GetMapping("/new")
    public String newPostulacionForm(Model model, Principal principal) throws BusinessException {
        if (principal == null) return "redirect:/petsit/home/login";
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        model.addAttribute("postulacion", new Postulacion());
        model.addAttribute("usuario", usuario);

        // ← CLAVE: solo muestra adopciones ACTIVAS en el select
        List<Adopcion> soloActivas = adopcionService.listActivas();
        model.addAttribute("listAdopciones", soloActivas);

        return "postulaciones/new_postulacion";
    }

    // ── Guardar ───────────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String savePostulacion(
            @ModelAttribute("postulacion") Postulacion postulacion,
            @RequestParam(value = "adopcion.id", required = false) Long adopcionId,
            Principal principal) {

        try {
            if (principal != null) {
                Usuario usuario = usuarioService.findByCorreo(principal.getName());
                postulacion.setUsuario(usuario);
            }
            if (adopcionId != null) {
                Adopcion adopcion = adopcionService.findById(adopcionId);
                // Doble check: no permitir postular a una adopción que no está activa
                if (!"ACTIVA".equals(adopcion.getEstado())) {
                    LOGGER.warn("Intento de postulación a adopción no activa: {}", adopcionId);
                    return "redirect:/petsit/usuarios/usuarioadoptar";
                }
                postulacion.setAdopcion(adopcion);
            }
            if (postulacion.getId() == null) postulacionService.save(postulacion);
            else postulacionService.update(postulacion);
        } catch (BusinessException e) {
            LOGGER.error("Error guardando postulación: {}", e.getMessage());
        }
        return "redirect:/petsit/usuarios/mis-solicitudes";
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id:\\d+}")
    public String deletePostulacion(@PathVariable Long id) {
        postulacionService.delete(id);
        return "redirect:/petsit/usuarios/mis-solicitudes";
    }
}
