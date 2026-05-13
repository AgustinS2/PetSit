package ar.edu.davinci.PetSit.controller.web.Alerta;

import ar.edu.davinci.PetSit.domain.*;
import ar.edu.davinci.PetSit.dto.ReporteMapaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.AlertaRepository;
import ar.edu.davinci.PetSit.service.Mascota.MascotaService;
import ar.edu.davinci.PetSit.service.Refugio.RefugioService;
import ar.edu.davinci.PetSit.service.Reporte.ReporteService;
import ar.edu.davinci.PetSit.service.Usuario.UsuarioService;
import ar.edu.davinci.PetSit.service.Veterinaria.VeterinariaService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Maneja tanto la vista del mapa como la API REST que consume el JS de Leaflet.
 *
 * GET  /petsit/usuarios/usuariomapa   → vista HTML del mapa
 * GET  /petsit/alertas/mapa           → alias (por compatibilidad con nav existente)
 * GET  /petsit/api/mapa/reportes      → JSON con todos los reportes para Leaflet
 * GET  /petsit/api/mapa/veterinarias  → JSON con veterinarias activas
 * GET  /petsit/api/mapa/refugios      → JSON con refugios
 * POST /petsit/api/mapa/reporte       → guarda un nuevo reporte desde el formulario del mapa
 */
@Controller
@RequestMapping("/petsit")
public class AlertaMapaController {

    private final Logger LOGGER = LoggerFactory.getLogger(AlertaMapaController.class);

    @Autowired private ReporteService     reporteService;
    @Autowired private UsuarioService     usuarioService;
    @Autowired private MascotaService     mascotaService;
    @Autowired private VeterinariaService veterinariaService;
    @Autowired private RefugioService     refugioService;
    @Autowired private AlertaRepository   alertaRepository;

    // ─────────────────────────────────────────────────────────────────────────
    //  VISTAS HTML
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/usuarios/usuariomapa")
    public String usuarioMapa(Model model, Principal principal) throws BusinessException {
        if (principal == null) return "redirect:/petsit/home/login";
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        model.addAttribute("usuario", usuario);
        // El JSON de reportes lo carga el JS via fetch /petsit/api/mapa/reportes
        return "usuarios/usuariomapa";
    }

    @GetMapping("/alertas/mapa")
    public String alertasMapa(Model model, Principal principal) throws BusinessException {
        // Alias para compatibilidad con el nav que aún apunta a este path
        return usuarioMapa(model, principal);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  API REST — el JS de Leaflet llama a estos endpoints con fetch()
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Devuelve todos los reportes con coordenadas como JSON.
     * El mapa los consume en el arranque y al refrescar.
     */
    @GetMapping("/api/mapa/reportes")
    @ResponseBody
    public ResponseEntity<List<ReporteMapaDTO>> getReportesParaMapa() {
        List<ReporteMapaDTO> reportes = reporteService.obtenerReportesParaMapa();
        return ResponseEntity.ok(reportes);
    }

    /**
     * Veterinarias activas con coordenadas para el mapa.
     * Si no tienen lat/lng en la BD, se muestran igual en el sidebar
     * pero no en el mapa.
     */
    @GetMapping("/api/mapa/veterinarias")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getVeterinariasParaMapa() {
        List<Veterinaria> vets = veterinariaService.list();
        List<Map<String, Object>> result = vets.stream()
                .filter(v -> Boolean.TRUE.equals(v.getActiva()))
                .map(v -> Map.<String, Object>of(
                        "id",        v.getId(),
                        "nombre",    v.getNombre()         != null ? v.getNombre()         : "",
                        "direccion", v.getDireccion()       != null ? v.getDireccion()      : "",
                        "telefono",  v.getTelefono()        != null ? v.getTelefono()       : "",
                        "horario",   v.getHorarioAtencion() != null ? v.getHorarioAtencion(): "",
                        "ubicacion", v.getUbicacion()       != null ? v.getUbicacion()      : "",
                        "tipo",      "VETERINARIA"
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

    /**
     * Refugios con datos para el mapa / sidebar.
     */
    @GetMapping("/api/mapa/refugios")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getRefugiosParaMapa() {
        List<Refugio> refugios = refugioService.list();
        List<Map<String, Object>> result = refugios.stream()
                .map(r -> Map.<String, Object>of(
                        "id",        r.getId(),
                        "nombre",    r.getNombre() != null ? r.getNombre() : "",
                        "direccion", r.getDireccion() != null ? r.getDireccion() : "",
                        "telefono",  r.getTelefono() != null ? r.getTelefono() : "",
                        "correo",    r.getCorreo() != null ? r.getCorreo() : "",
                        "ubicacion", r.getUbicacion() != null ? r.getUbicacion() : "",
                        "tipo",      "REFUGIO"
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  GUARDAR REPORTE DESDE EL FORMULARIO DEL MAPA
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * El formulario del mapa (reportForm) hace un POST JSON a este endpoint.
     * El JS serializa el form y envía:
     *   { tipoReporte, descripcion, ubicacion, lat, lng, foto }
     *
     * Responde JSON { ok: true, id: <id> } o { ok: false, error: "..." }
     */
    @PostMapping("/api/mapa/reporte")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarReporte(
            @RequestBody Map<String, Object> body,
            Principal principal) {

        try {
            // Resolver usuario (puede ser anónimo, Reporte.usuario es nullable)
            Usuario usuario = null;
            if (principal != null) {
                try {
                    usuario = usuarioService.findByCorreo(principal.getName());
                } catch (Exception ignored) {}
            }

            // Parsear tipoReporte
            String tipoStr = (String) body.getOrDefault("tipoReporte", "MASCOTA_PERDIDA");
            TipoReporte tipoReporte;
            try {
                tipoReporte = TipoReporte.valueOf(tipoStr);
            } catch (IllegalArgumentException e) {
                tipoReporte = TipoReporte.MASCOTA_PERDIDA;
            }

            // Coordenadas
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

            // prePersist setea estadoMascota automáticamente
            reporte.actualizarEstadoSegunTipo();

            Reporte saved = reporteService.save(reporte);
            LOGGER.info("Nuevo reporte guardado id={} tipo={}", saved.getId(), tipoReporte);

            return ResponseEntity.ok(Map.of("ok", true, "id", saved.getId()));

        } catch (Exception e) {
            LOGGER.error("Error guardando reporte: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", e.getMessage()));
        }
    }
}
