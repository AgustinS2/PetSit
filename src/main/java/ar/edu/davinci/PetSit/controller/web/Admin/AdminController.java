package ar.edu.davinci.PetSit.controller.web.Admin;

import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.service.Adopcion.AdopcionService;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Postulacion.PostulacionService;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Reporte.ReporteService;
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
public class AdminController {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired private UsuarioService     usuarioService;
    @Autowired private MascotaService     mascotaService;
    @Autowired private RefugioService     refugioService;
    @Autowired private VeterinariaService veterinariaService;
    @Autowired private AdopcionService    adopcionService;
    @Autowired private PostulacionService postulacionService;
    @Autowired private ReporteService     reporteService;
    @Autowired private PasswordEncoder    passwordEncoder;

    // ── Helper: carga contadores de pendientes en todos los modelos ──────────
    private void addPendientes(Model model) {
        try {
            model.addAttribute("pendientesVet", veterinariaService.countPendientes());
            model.addAttribute("pendientesRef", refugioService.countPendientes());
        } catch (Exception e) {
            model.addAttribute("pendientesVet", 0);
            model.addAttribute("pendientesRef", 0);
        }
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────────────
    @GetMapping({"", "/", "/index"})
    public String index(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario",           usuarioService.findByCorreo(principal.getName()));
        model.addAttribute("totalUsuarios",     usuarioService.count());
        model.addAttribute("totalMascotas",     mascotaService.count());
        model.addAttribute("totalRefugios",     refugioService.count());
        model.addAttribute("totalVeterinarias", veterinariaService.count());
        model.addAttribute("totalAdopciones",   adopcionService.count());
        addPendientes(model);
        return "admin/index";
    }

    // ── USUARIOS ──────────────────────────────────────────────────────────────
    @GetMapping("/usuarios")
    public String seccionUsuarios(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        Pageable p = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("listUsuarios", usuarioService.list(p).getContent());
        addPendientes(model);
        return "admin/usuarios/index";
    }

    @GetMapping("/usuarios/edit/{id:\\d+}")
    public String editUsuarioForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuarioEdit",  usuarioService.findById(id));
        model.addAttribute("tipoUsuarios", TipoUsuario.getTipoUsuarios());
        model.addAttribute("usuario",      usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_usuario";
    }

    @PostMapping("/usuarios/save")
    public String saveUsuario(@ModelAttribute("usuarioEdit") Usuario u) {
        try { usuarioService.update(u); } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/usuarios";
    }

    @GetMapping("/usuarios/delete/{id:\\d+}")
    public String deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return "redirect:/petsit/admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo-admin")
    public String nuevoAdminForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("nuevoAdmin", new Usuario());
        model.addAttribute("usuario",    usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_nuevo_admin";
    }

    @PostMapping("/usuarios/nuevo-admin/save")
    public String saveNuevoAdmin(@ModelAttribute("nuevoAdmin") Usuario nuevoAdmin) {
        try {
            nuevoAdmin.setTipo(TipoUsuario.ADMINISTRADOR);
            nuevoAdmin.setContrasena(passwordEncoder.encode(nuevoAdmin.getContrasena()));
            usuarioService.save(nuevoAdmin);
        } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/usuarios";
    }

    // ── MASCOTAS ──────────────────────────────────────────────────────────────
    @GetMapping("/mascotas")
    public String seccionMascotas(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        Pageable p = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("listMascotas", mascotaService.list(p).getContent());
        addPendientes(model);
        return "admin/mascotas/index";
    }

    // ── VETERINARIAS ──────────────────────────────────────────────────────────
    @GetMapping("/veterinarias")
    public String seccionVeterinarias(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario",        usuarioService.findByCorreo(principal.getName()));
        model.addAttribute("listPendientes", veterinariaService.listPendientes());
        model.addAttribute("listAprobadas",  veterinariaService.listAprobadas());
        addPendientes(model);
        return "admin/veterinarias/index";
    }

    @PostMapping("/veterinarias/{id:\\d+}/aprobar")
    public String aprobarVeterinaria(@PathVariable Long id) throws BusinessException {
        Veterinaria v = veterinariaService.findById(id);
        v.setEstadoAprobacion("APROBADA");
        v.setActiva(true);
        veterinariaService.update(v);
        return "redirect:/petsit/admin/veterinarias";
    }

    @PostMapping("/veterinarias/{id:\\d+}/rechazar")
    public String rechazarVeterinaria(@PathVariable Long id) throws BusinessException {
        Veterinaria v = veterinariaService.findById(id);
        v.setEstadoAprobacion("RECHAZADA");
        v.setActiva(false);
        veterinariaService.update(v);
        return "redirect:/petsit/admin/veterinarias";
    }

    @GetMapping("/veterinarias/new")
    public String newVetForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("veterinaria", new Veterinaria());
        model.addAttribute("usuario",     usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_veterinaria";
    }

    @PostMapping("/veterinarias/save")
    public String saveVet(@ModelAttribute Veterinaria v) {
        try {
            v.setEstadoAprobacion("APROBADA");
            if (v.getId() == null) veterinariaService.save(v);
            else veterinariaService.update(v);
        } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/veterinarias";
    }

    @GetMapping("/veterinarias/edit/{id:\\d+}")
    public String editVetForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("veterinaria", veterinariaService.findById(id));
        model.addAttribute("usuario",     usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_veterinaria";
    }

    @GetMapping("/veterinarias/delete/{id:\\d+}")
    public String deleteVet(@PathVariable Long id) {
        veterinariaService.delete(id);
        return "redirect:/petsit/admin/veterinarias";
    }

    // ── REFUGIOS ──────────────────────────────────────────────────────────────
    @GetMapping("/refugios")
    public String seccionRefugios(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario",        usuarioService.findByCorreo(principal.getName()));
        model.addAttribute("listPendientes", refugioService.listPendientes());
        model.addAttribute("listAprobadas",  refugioService.listAprobadas());
        addPendientes(model);
        return "admin/refugios/index";
    }

    @PostMapping("/refugios/{id:\\d+}/aprobar")
    public String aprobarRefugio(@PathVariable Long id) throws BusinessException {
        Refugio r = refugioService.findById(id);
        r.setEstadoAprobacion("APROBADA");
        r.setActiva(true);
        refugioService.update(r);
        return "redirect:/petsit/admin/refugios";
    }

    @PostMapping("/refugios/{id:\\d+}/rechazar")
    public String rechazarRefugio(@PathVariable Long id) throws BusinessException {
        Refugio r = refugioService.findById(id);
        r.setEstadoAprobacion("RECHAZADA");
        r.setActiva(false);
        refugioService.update(r);
        return "redirect:/petsit/admin/refugios";
    }

    @GetMapping("/refugios/new")
    public String newRefugioForm(Model model, Principal principal) throws BusinessException {
        model.addAttribute("refugio", new Refugio());
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_refugio";
    }

    @PostMapping("/refugios/save")
    public String saveRefugio(@ModelAttribute Refugio r) {
        try {
            r.setEstadoAprobacion("APROBADA");
            r.setActiva(true);
            if (r.getId() == null) refugioService.save(r);
            else refugioService.update(r);
        } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/refugios";
    }

    @GetMapping("/refugios/edit/{id:\\d+}")
    public String editRefugioForm(@PathVariable Long id, Model model, Principal principal) throws BusinessException {
        model.addAttribute("refugio", refugioService.findById(id));
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        addPendientes(model);
        return "admin/form_refugio";
    }

    @GetMapping("/refugios/delete/{id:\\d+}")
    public String deleteRefugio(@PathVariable Long id) {
        refugioService.delete(id);
        return "redirect:/petsit/admin/refugios";
    }

    // ── ADOPCIONES ────────────────────────────────────────────────────────────
    @GetMapping("/adopciones")
    public String seccionAdopciones(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        Pageable p = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("listAdopciones",    adopcionService.list(p).getContent());
        model.addAttribute("listPostulaciones", postulacionService.list(p).getContent());
        addPendientes(model);
        return "admin/adopciones/index";
    }

    @GetMapping("/adopciones/delete/{id:\\d+}")
    public String deleteAdopcion(@PathVariable Long id) {
        adopcionService.delete(id);
        return "redirect:/petsit/admin/adopciones";
    }

    @PostMapping("/adopciones/estado/{id:\\d+}")
    public String cambiarEstadoAdopcion(@PathVariable Long id, @RequestParam String estado) throws BusinessException {
        Adopcion a = adopcionService.findById(id);
        a.setEstado(estado);
        try { adopcionService.update(a); } catch (BusinessException e) { LOGGER.error(e.getMessage()); }
        return "redirect:/petsit/admin/adopciones";
    }

    // ── REPORTES ──────────────────────────────────────────────────────────────
    @GetMapping("/reportes")
    public String seccionReportes(Model model, Principal principal) throws BusinessException {
        model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        Pageable p = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("listReportes", reporteService.list(p).getContent());
        addPendientes(model);
        return "admin/reportes/index";
    }

    @GetMapping("/reportes/delete/{id:\\d+}")
    public String deleteReporte(@PathVariable Long id) {
        reporteService.delete(id);
        return "redirect:/petsit/admin/reportes";
    }
}
