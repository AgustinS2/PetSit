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
    public String newMascotaForm(Model model, java.security.Principal principal) {
        LOGGER.info("GET /petsit/mascotas/new");
        model.addAttribute("mascota", new Mascota());
        // Pasar el usuario logueado para asignarlo como dueño automáticamente
        if (principal != null) {
            try {
                model.addAttribute("usuarioLogueado", usuarioService.findByCorreo(principal.getName()));
            } catch (Exception ignored) {}
        }
        return "mascotas/new_mascotas";
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
            @RequestParam(value = "fotoFile", required = false) org.springframework.web.multipart.MultipartFile fotoFile,
            Principal principal) {

        LOGGER.info("POST /petsit/mascotas/save — id mascota: {}", mascota.getId());

        try {
            // Foto: si se subió archivo, guardarlo y setear el path
            if (fotoFile != null && !fotoFile.isEmpty()) {
                java.nio.file.Path uploadPath = java.nio.file.Paths.get("src/main/resources/static/assets/img/uploads/");
                if (!java.nio.file.Files.exists(uploadPath)) java.nio.file.Files.createDirectories(uploadPath);
                String orig = fotoFile.getOriginalFilename();
                String ext  = (orig != null && orig.contains(".")) ? orig.substring(orig.lastIndexOf('.')) : ".jpg";
                String filename = "msc_" + System.currentTimeMillis() + ext;
                java.nio.file.Files.copy(fotoFile.getInputStream(), uploadPath.resolve(filename), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                mascota.setFoto("uploads/" + filename);
            }

            // Resolver el dueño: si no viene ID del select, usar el usuario logueado
            if (duenoId != null) {
                mascota.setDueno(usuarioService.findById(duenoId));
            } else if (mascota.getDueno() == null && principal != null) {
                mascota.setDueno(usuarioService.findByCorreo(principal.getName()));
            }

            if (mascota.getId() == null) {
                mascotaService.save(mascota);
            } else {
                mascotaService.update(mascota);
            }
        } catch (BusinessException e) {
            LOGGER.error("Error guardando mascota: {}", e.getMessage());
        } catch (java.io.IOException e) {
            LOGGER.error("Error guardando foto mascota: {}", e.getMessage());
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
