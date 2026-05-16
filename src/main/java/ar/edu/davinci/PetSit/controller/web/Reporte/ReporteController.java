package ar.edu.davinci.PetSit.controller.web.Reporte;

import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.ReporteRepository;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Reporte.ReporteService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/petsit/reportes")
public class ReporteController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReporteController.class);
    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/uploads/";

    @Autowired private ReporteService reporteService;
    @Autowired private ReporteRepository reporteRepository;
    @Autowired private UsuarioService usuarioService;
    @Autowired private MascotaService mascotaService;

    // ── LISTADO (para admin) ──────────────────────────────────
    @GetMapping("/mis-reportes")
    public String misReportes(Model model, Principal principal) throws ar.edu.davinci.PetSit.exceptions.BusinessException {
        if (principal == null) return "redirect:/petsit/home/login";
        ar.edu.davinci.PetSit.domain.Usuario usuario = usuarioService.findByCorreo(principal.getName());
        model.addAttribute("usuario", usuario);
        // Solo los reportes del usuario logueado, ordenados por fecha desc
        model.addAttribute("listReportes", reporteRepository.findByUsuarioOrderByFechaDesc(usuario));
        return "reportes/mis_reportes";
    }

    @GetMapping("/list")
    public String listReportes(Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Reporte> reportes = reporteService.list(pageable);
        model.addAttribute("listReportes", reportes.getContent());
        return "reportes/list_reportes";
    }

    // ── FORMULARIO: MASCOTA PERDIDA ───────────────────────────
    @GetMapping("/new/perdido")
    public String newReportePerdido(Model model, Principal principal) {
        if (principal != null) {
            try {
                ar.edu.davinci.PetSit.domain.Usuario usuario = usuarioService.findByCorreo(principal.getName());
                model.addAttribute("usuario", usuario);
                // Si es dueño, pasar sus mascotas para el selector
                if (ar.edu.davinci.PetSit.domain.TipoUsuario.DUENO.equals(usuario.getTipo())) {
                    model.addAttribute("misMascotas", mascotaService.findByDueno(usuario));
                }
            } catch (Exception ignored) {}
        }
        model.addAttribute("reporte", new Reporte());
        return "reportes/new_reporte_perdido";
    }

    // ── FORMULARIO: MASCOTA ENCONTRADA ────────────────────────
    @GetMapping("/new/encontrado")
    public String newReporteEncontrado(Model model, Principal principal) {
        if (principal != null) {
            try { model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName())); }
            catch (Exception ignored) {}
        }
        model.addAttribute("reporte", new Reporte());
        return "reportes/new_reporte_encontrado";
    }

    // ── GUARDAR REPORTE PERDIDO ───────────────────────────────
    @PostMapping("/save/perdido")
    public String saveReportePerdido(
            @RequestParam("nombre")        String nombre,
            @RequestParam("especie")       String especie,
            @RequestParam(value="raza",    required=false) String raza,
            @RequestParam(value="tamano",  required=false) String tamano,
            @RequestParam(value="color",   required=false) String color,
            @RequestParam("descripcion")   String descripcion,
            @RequestParam("zona")          String zona,
            @RequestParam("fecha")         String fecha,
            @RequestParam(value="ubicacion", required=false) String ubicacion,
            @RequestParam("telefono")      String telefono,
            @RequestParam(value="fotoFile", required=false) MultipartFile fotoFile,
            Principal principal) {

        try {
            String fotoPath = guardarFotoSiExiste(fotoFile, "rep_");
            String desc = buildDescripcion(nombre, especie, raza, tamano, color, descripcion, telefono);

            Reporte r = Reporte.builder()
                    .tipoReporte(TipoReporte.MASCOTA_PERDIDA)
                    .estadoMascota(EstadoMascota.PERDIDO)
                    .descripcion(desc)
                    .ubicacion(zona + (ubicacion != null ? " - " + ubicacion : ""))
                    .foto(fotoPath)
                    .estado("PENDIENTE")
                    .fecha(LocalDateTime.now())
                    .build();

            if (principal != null) {
                try { r.setUsuario(usuarioService.findByCorreo(principal.getName())); }
                catch (Exception ignored) {}
            }

            reporteService.save(r);
            return "redirect:/petsit/usuarios/usuariomapa";
        } catch (Exception e) {
            LOGGER.error("Error guardando reporte perdido: {}", e.getMessage());
            return "redirect:/petsit/reportes/new/perdido?error=true";
        }
    }

    // ── GUARDAR REPORTE ENCONTRADO ────────────────────────────
    @PostMapping("/save/encontrado")
    public String saveReporteEncontrado(
            @RequestParam("especie")       String especie,
            @RequestParam(value="raza",    required=false) String raza,
            @RequestParam(value="tamano",  required=false) String tamano,
            @RequestParam(value="color",   required=false) String color,
            @RequestParam("descripcion")   String descripcion,
            @RequestParam("zona")          String zona,
            @RequestParam("fecha")         String fecha,
            @RequestParam(value="ubicacion", required=false) String ubicacion,
            @RequestParam("telefono")      String telefono,
            @RequestParam("situacion")     String situacion,
            @RequestParam(value="fotoFile", required=false) MultipartFile fotoFile,
            Principal principal) {

        try {
            String fotoPath = guardarFotoSiExiste(fotoFile, "rep_");
            TipoReporte tipo = "TENGO_CONMIGO".equals(situacion)
                    ? TipoReporte.TENGO_CONMIGO
                    : TipoReporte.SOLO_LO_VI;

            String desc = buildDescripcion(null, especie, raza, tamano, color, descripcion, telefono);

            Reporte r = Reporte.builder()
                    .tipoReporte(tipo)
                    .estadoMascota(EstadoMascota.ENCONTRADO)
                    .descripcion(desc)
                    .ubicacion(zona + (ubicacion != null ? " - " + ubicacion : ""))
                    .foto(fotoPath)
                    .estado("PENDIENTE")
                    .fecha(LocalDateTime.now())
                    .build();

            if (principal != null) {
                try { r.setUsuario(usuarioService.findByCorreo(principal.getName())); }
                catch (Exception ignored) {}
            }

            reporteService.save(r);
            return "redirect:/petsit/usuarios/usuariomapa";
        } catch (Exception e) {
            LOGGER.error("Error guardando reporte encontrado: {}", e.getMessage());
            return "redirect:/petsit/reportes/new/encontrado?error=true";
        }
    }

    // ── Helpers ───────────────────────────────────────────────
    private String guardarFotoSiExiste(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) return null;
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String ext = ".jpg";
        String orig = file.getOriginalFilename();
        if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf('.'));
        String filename = prefix + System.currentTimeMillis() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "uploads/" + filename;
    }

    private String buildDescripcion(String nombre, String especie, String raza,
                                    String tamano, String color, String descripcion, String telefono) {
        StringBuilder sb = new StringBuilder();
        if (nombre  != null && !nombre.isBlank())  sb.append(nombre).append(" · ");
        if (especie != null && !especie.isBlank())  sb.append(especie).append(" · ");
        if (raza    != null && !raza.isBlank())     sb.append(raza).append(" · ");
        if (tamano  != null && !tamano.isBlank())   sb.append(tamano).append(" · ");
        if (color   != null && !color.isBlank())    sb.append(color).append(" · ");
        sb.append(descripcion);
        if (telefono != null && !telefono.isBlank()) sb.append(" · Tel: ").append(telefono);
        return sb.toString();
    }
}
