package ar.edu.davinci.PetSit.controller.web.Alerta;

import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.dto.ReporteMapaDTO;
import ar.edu.davinci.PetSit.repository.AlertaRepository;
import ar.edu.davinci.PetSit.service.Reporte.ReporteService;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/petsit")
@RequiredArgsConstructor
public class AlertaMapaController {

    private final Logger LOGGER = LoggerFactory.getLogger(AlertaMapaController.class);

    private final AlertaRepository    alertaRepository;
    private final ReporteService      reporteService;
    private final VeterinariaService  veterinariaService;
    private final RefugioService      refugioService;
    private final UsuarioService      usuarioService;

    // ── VISTAS ────────────────────────────────────────────────────────────────
    @GetMapping("/usuarios/usuariomapa")
    public String usuarioMapa(Model model, Principal principal) {
        cargarUsuario(model, principal);
        return "usuarios/usuariomapa";
    }

    @GetMapping("/alertas/mapa")
    public String alertasMapa(Model model, Principal principal) {
        cargarUsuario(model, principal);
        return "usuarios/usuariomapa";
    }

    private void cargarUsuario(Model model, Principal principal) {
        if (principal == null) return;
        try {
            model.addAttribute("usuario", usuarioService.findByCorreo(principal.getName()));
        } catch (Exception e) {
            LOGGER.warn("No se pudo cargar el usuario del mapa: {}", e.getMessage());
        }
    }

    // ── API REPORTES ──────────────────────────────────────────────────────────
    @GetMapping("/api/mapa/reportes")
    @ResponseBody
    public ResponseEntity<List<ReporteMapaDTO>> getReportesParaMapa() {
        return ResponseEntity.ok(reporteService.obtenerReportesParaMapa());
    }

    // ── API VETERINARIAS ──────────────────────────────────────────────────────
    // Solo las que están: activa=true Y estadoAprobacion=APROBADA Y tienen coordenadas
    @GetMapping("/api/mapa/veterinarias")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getVeterinariasParaMapa() {
        List<Veterinaria> vets = veterinariaService.list();
        List<Map<String, Object>> result = vets.stream()
                .filter(v -> Boolean.TRUE.equals(v.getActiva()))
                .filter(v -> "APROBADA".equals(v.getEstadoAprobacion()))
                .filter(v -> v.getLat() != null && v.getLng() != null)
                .map(v -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id",        v.getId());
                    m.put("nombre",    v.getNombre()          != null ? v.getNombre()          : "");
                    m.put("direccion", v.getDireccion()       != null ? v.getDireccion()       : "");
                    m.put("telefono",  v.getTelefono()        != null ? v.getTelefono()        : "");
                    m.put("horario",   v.getHorarioAtencion() != null ? v.getHorarioAtencion() : "");
                    m.put("ubicacion", v.getUbicacion()       != null ? v.getUbicacion()       : "");
                    m.put("lat",       v.getLat());
                    m.put("lng",       v.getLng());
                    m.put("tipo",      "VETERINARIA");
                    return m;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    // ── API REFUGIOS ──────────────────────────────────────────────────────────
    // Solo los que están: activa=true Y estadoAprobacion=APROBADA Y tienen coordenadas
    @GetMapping("/api/mapa/refugios")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getRefugiosParaMapa() {
        List<Refugio> refugios = refugioService.list();
        List<Map<String, Object>> result = refugios.stream()
                .filter(r -> Boolean.TRUE.equals(r.getActiva()))
                .filter(r -> "APROBADA".equals(r.getEstadoAprobacion()))
                .filter(r -> r.getLat() != null && r.getLng() != null)
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id",        r.getId());
                    m.put("nombre",    r.getNombre()    != null ? r.getNombre()    : "");
                    m.put("direccion", r.getDireccion() != null ? r.getDireccion() : "");
                    m.put("telefono",  r.getTelefono()  != null ? r.getTelefono()  : "");
                    m.put("correo",    r.getCorreo()    != null ? r.getCorreo()    : "");
                    m.put("ubicacion", r.getUbicacion() != null ? r.getUbicacion() : "");
                    m.put("lat",       r.getLat());
                    m.put("lng",       r.getLng());
                    m.put("tipo",      "REFUGIO");
                    return m;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    // ── GUARDAR REPORTE ───────────────────────────────────────────────────────
    @PostMapping("/api/mapa/reporte")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarReporte(
            @RequestBody Map<String, Object> body,
            Principal principal) {
        try {
            Usuario usuario = null;
            if (principal != null) {
                try { usuario = usuarioService.findByCorreo(principal.getName()); }
                catch (Exception ignored) {}
            }

            String tipoStr = (String) body.getOrDefault("tipoReporte", "MASCOTA_PERDIDA");
            TipoReporte tipoReporte;
            try { tipoReporte = TipoReporte.valueOf(tipoStr); }
            catch (IllegalArgumentException e) { tipoReporte = TipoReporte.MASCOTA_PERDIDA; }

            Double lat = body.get("lat") != null ? Double.parseDouble(body.get("lat").toString()) : null;
            Double lng = body.get("lng") != null ? Double.parseDouble(body.get("lng").toString()) : null;

            Reporte reporte = Reporte.builder()
                    .usuario(usuario)
                    .tipoReporte(tipoReporte)
                    .descripcion((String) body.getOrDefault("descripcion", ""))
                    .ubicacion((String) body.getOrDefault("ubicacion", ""))
                    .foto((String) body.getOrDefault("foto", null))
                    .lat(lat)
                    .lng(lng)
                    .fecha(LocalDateTime.now())
                    .estado("PENDIENTE")
                    .build();

            reporte.actualizarEstadoSegunTipo();
            Reporte saved = reporteService.save(reporte);
            LOGGER.info("Nuevo reporte guardado id={} tipo={}", saved.getId(), tipoReporte);

            return ResponseEntity.ok(Map.of("ok", true, "id", saved.getId()));
        } catch (Exception e) {
            LOGGER.error("Error guardando reporte: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", e.getMessage()));
        }
    }
}
