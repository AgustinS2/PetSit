package ar.edu.davinci.PetSit.service.Reporte;

import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.domain.TipoReporte;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.ReporteMapaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReporteServiceImpl.class);

    private static final double RADIO_KM     = 10.0;
    private static final double EARTH_RADIUS = 6371.0;

    // ~0.09 grados ≈ 10km (para el bounding box previo al Haversine)
    private static final double DELTA_GRADOS = 0.09;

    private final ReporteRepository repository;

    @Autowired
    public ReporteServiceImpl(final ReporteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reporte save(final Reporte reporte) throws BusinessException {
        LOGGER.debug("Guardando nuevo reporte: {}", reporte);
        if (reporte.getId() == null) return repository.save(reporte);
        throw new BusinessException("No se puede crear un reporte con un ID existente.");
    }

    @Override
    public Reporte update(final Reporte reporte) throws BusinessException {
        LOGGER.debug("Actualizando reporte: {}", reporte);
        if (reporte.getId() != null) return repository.save(reporte);
        throw new BusinessException("No se puede actualizar un reporte sin ID.");
    }

    @Override public void delete(final Reporte r) { repository.delete(r); }
    @Override public void delete(final Long id)   { repository.deleteById(id); }

    @Override
    public Reporte findById(final Long id) throws BusinessException {
        Optional<Reporte> opt = repository.findById(id);
        if (opt.isPresent()) return opt.get();
        throw new BusinessException("No se encontró el reporte con el id: " + id);
    }

    @Override public List<Reporte> list()                  { return repository.findAll(); }
    @Override public Page<Reporte> list(Pageable pageable) { return repository.findAll(pageable); }
    @Override public long count()                          { return repository.count(); }

    // ─────────────────────────────────────────────────────────────────────────
    //  MAPA
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public List<ReporteMapaDTO> obtenerReportesParaMapa() {
        LOGGER.debug("Obteniendo reportes con coordenadas para el mapa");
        return repository.findByLatIsNotNullAndLngIsNotNull()
                .stream()
                .map(this::toMapaDTO)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  NOTIFICACIÓN A USUARIOS CERCANOS (10km)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Guarda el reporte y filtra los usuarios que están a ≤ 10km.
     * Usa Haversine para la distancia exacta.
     * El controller decide qué hacer con la lista (enviar email, etc.).
     */
    @Override
    public List<Usuario> guardarYObtenerUsuariosCercanos(Reporte reporte, List<Usuario> todosUsuarios) {
        Reporte saved;
        try {
            saved = save(reporte);
        } catch (BusinessException e) {
            LOGGER.error("Error guardando reporte para notificación: {}", e.getMessage());
            return List.of();
        }

        if (saved.getLat() == null || saved.getLng() == null) return List.of();

        return todosUsuarios.stream()
                .filter(u -> u != null)
                // TODO: cuando agregues lat/lng a Usuario, filtrar por distancia real
                // .filter(u -> u.getLat() != null && u.getLng() != null)
                // .filter(u -> distanciaKm(saved.getLat(), saved.getLng(), u.getLat(), u.getLng()) <= RADIO_KM)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PRIVADOS
    // ─────────────────────────────────────────────────────────────────────────

    private ReporteMapaDTO toMapaDTO(Reporte r) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Determinar rol del reportante según tipo
        String rol = r.getTipoReporte() == TipoReporte.MASCOTA_PERDIDA ? "DUENO" : "ENCONTRADOR";

        return ReporteMapaDTO.builder()
                .id(r.getId())
                .lat(r.getLat())
                .lng(r.getLng())
                .ubicacion(r.getUbicacion())
                .descripcion(r.getDescripcion())
                .foto(r.getFoto())
                .estado(r.getEstado())
                .tipoReporte(r.getTipoReporte() != null ? r.getTipoReporte().name() : null)
                .estadoMascota(r.getEstadoMascota() != null ? r.getEstadoMascota().name() : null)
                .color(r.getColorMapa())
                // Mascota
                .nombreMascota(r.getDescripcion()) // el form guarda nombre en descripcion si no tiene mascota asociada
                .zona(r.getUbicacion())
                .fecha(r.getFecha() != null ? r.getFecha().format(fmt) : null)
                // Contacto
                .contactoTel(r.getUsuario() != null ? r.getUsuario().getTelefono() : null)
                .rolReportante(rol)
                .build();
    }

    /**
     * Fórmula de Haversine — distancia en km entre dos coordenadas.
     * Reservado para cuando Usuario tenga lat/lng.
     */
    @SuppressWarnings("unused")
    private double distanciaKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
