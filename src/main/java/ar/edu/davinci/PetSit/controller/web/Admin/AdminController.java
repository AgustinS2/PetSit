package ar.edu.davinci.PetSit.controller.web.Admin;

import ar.edu.davinci.PetSit.controller.PetSitApp;
import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/petsit/admin")
public class AdminController extends PetSitApp {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired private UsuarioService     usuarioService;
    @Autowired private MascotaService     mascotaService;
    @Autowired private RefugioService     refugioService;
    @Autowired private VeterinariaService veterinariaService;
    @Autowired private AdopcionService    adopcionService;
    @Autowired private PostulacionService postulacionService;
    @Autowired private PasswordEncoder    passwordEncoder;

    /** Máximo de registros por sección en el dashboard */
    private static final int LIMIT = 7;

    // ── INDEX ADMIN ───────────────────────────────────────────────────────────
    @GetMapping({"", "/", "/index"})
    public String index(Model model, Principal principal) throws BusinessException {
        Usuario admin = usuarioService.findByCorreo(principal.getName());
        model.addAttribute("usuario", admin);

        // Totales (para los stats cards — muestran el número real, no truncado)
        model.addAttribute("totalUsuarios",     usuarioService.count());
        model.addAttribute("totalMascotas",     mascotaService.count());
        model.addAttribute("totalRefugios",     refugioService.count());
        model.addAttribute("totalVeterinarias", veterinariaService.count());
        model.addAttribute("totalAdopciones",   adopcionService.count());

        // Los 7 más recientes de cada categoría (orden: id DESC = más nuevo primero)
        Pageable ultimos = PageRequest.of(0, LIMIT, Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("listUsuarios",      usuarioService.list(ultimos).getContent());
        model.addAttribute("listMascotas",      mascotaService.list(ultimos).getContent());
        model.addAttribute("listRefugios",      refugioService.list(ultimos).getContent());
        model.addAttribute("listVeterinarias",  veterinariaService.list(ultimos).getContent());
        model.addAttribute("listAdopciones",    adopcionService.list(ultimos).getContent());
        model.addAttribute("listPostulaciones", postulacionService.list(ultimos).getContent());

        return "admin/index";
    }

    // ── VETERINARIAS ──────────────────────────────────────────────────────────
    @GetMapping("/veterinarias/new")
    public String newVetForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("veterinaria", new Veterinaria());
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        return "admin/form_veterinaria";
    }

    @PostMapping("/veterinarias/save")
    public String saveVet(@ModelAttribute Veterinaria veterinaria) {
        try {
            if (veterinaria.getId() == null) veterinariaService.save(veterinaria);
            else veterinariaService.update(veterinaria);
        } catch (BusinessException e) { LOGGER.error("Error vet: {}", e.getMessage()); }
        return "redirect:/petsit/admin/index";
    }

    @GetMapping("/veterinarias/edit/{id:\\d+}")
    public String editVetForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("veterinaria", veterinariaService.findById(id));
        model.addAttribute("usuario",     usuarioService.findByCorreo(principal.getName()));
        return "admin/form_veterinaria";
    }

    @GetMapping("/veterinarias/delete/{id:\\d+}")
    public String deleteVet(@PathVariable Long id) {
        veterinariaService.delete(id);
        return "redirect:/petsit/admin/index";
    }

    // ── REFUGIOS ──────────────────────────────────────────────────────────────
    @GetMapping("/refugios/new")
    public String newRefugioForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("refugio", new Refugio());
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        return "admin/form_refugio";
    }

    @PostMapping("/refugios/save")
    public String saveRefugio(@ModelAttribute Refugio refugio) {
        try {
            if (refugio.getId() == null) refugioService.save(refugio);
            else refugioService.update(refugio);
        } catch (BusinessException e) { LOGGER.error("Error refugio: {}", e.getMessage()); }
        return "redirect:/petsit/admin/index";
    }

    @GetMapping("/refugios/edit/{id:\\d+}")
    public String editRefugioForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("refugio", refugioService.findById(id));
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        return "admin/form_refugio";
    }

    @GetMapping("/refugios/delete/{id:\\d+}")
    public String deleteRefugio(@PathVariable Long id) {
        refugioService.delete(id);
        return "redirect:/petsit/admin/index";
    }

    // ── USUARIOS ──────────────────────────────────────────────────────────────
    @GetMapping("/usuarios/edit/{id:\\d+}")
    public String editUsuarioForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuarioEdit",  usuarioService.findById(id));
        model.addAttribute("tipoUsuarios", TipoUsuario.getTipoUsuarios());
        model.addAttribute("usuario",      usuarioService.findByCorreo(principal.getName()));
        return "admin/form_usuario";
    }

    @PostMapping("/usuarios/save")
    public String saveUsuario(@ModelAttribute("usuarioEdit") Usuario usuarioEdit) {
        try {
            usuarioService.update(usuarioEdit);
        } catch (BusinessException e) { LOGGER.error("Error usuario: {}", e.getMessage()); }
        return "redirect:/petsit/admin/index";
    }

    @GetMapping("/usuarios/delete/{id:\\d+}")
    public String deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return "redirect:/petsit/admin/index";
    }

    @GetMapping("/usuarios/nuevo-admin")
    public String nuevoAdminForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("nuevoAdmin", new Usuario());
        model.addAttribute("usuario",    usuarioService.findByCorreo(principal.getName()));
        return "admin/form_nuevo_admin";
    }

    @PostMapping("/usuarios/nuevo-admin/save")
    public String saveNuevoAdmin(@ModelAttribute("nuevoAdmin") Usuario nuevoAdmin) {
        try {
            nuevoAdmin.setTipo(TipoUsuario.ADMINISTRADOR);
            nuevoAdmin.setContrasena(passwordEncoder.encode(nuevoAdmin.getContrasena()));
            usuarioService.save(nuevoAdmin);
            LOGGER.info("Nuevo administrador creado: {}", nuevoAdmin.getCorreo());
        } catch (BusinessException e) {
            LOGGER.error("Error creando admin: {}", e.getMessage());
        }
        return "redirect:/petsit/admin/index";
    }

    // ── ADOPCIONES ────────────────────────────────────────────────────────────
    @GetMapping("/adopciones/delete/{id:\\d+}")
    public String deleteAdopcion(@PathVariable Long id) {
        adopcionService.delete(id);
        return "redirect:/petsit/admin/index";
    }

    @PostMapping("/adopciones/estado/{id:\\d+}")
    public String cambiarEstadoAdopcion(@PathVariable Long id, @RequestParam String estado) throws BusinessException {
        Adopcion a = adopcionService.findById(id);
        a.setEstado(estado);
        try { adopcionService.update(a); } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/index";
    }
}
