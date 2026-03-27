package ar.edu.davinci.PetSit.service.Reporte;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.dto.ReporteMapaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.ReporteRepository;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReporteServiceImpl.class);
    private final ReporteRepository repository;

    @Autowired
    public ReporteServiceImpl(final ReporteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reporte save(final Reporte reporte) throws BusinessException {
        LOGGER.debug("Guardando nuevo reporte: {}", reporte);
        if (reporte.getId() == null) {
            return repository.save(reporte);
        }
        throw new BusinessException("No se puede crear un reporte con un ID existente.");
    }

    @Override
    public Reporte update(final Reporte reporte) throws BusinessException {
        LOGGER.debug("Actualizando reporte: {}", reporte);
        if (reporte.getId() != null) {
            return repository.save(reporte);
        }
        throw new BusinessException("No se puede actualizar un reporte sin ID.");
    }

    @Override
    public void delete(final Reporte reporte) {
        LOGGER.debug("Eliminando reporte: {}", reporte);
        repository.delete(reporte);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Eliminando reporte por id: {}", id);
        repository.deleteById(id);
    }

    @Override
    public Reporte findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscando reporte por id: {}", id);
        Optional<Reporte> reporteOptional = repository.findById(id);
        if (reporteOptional.isPresent()) {
            return reporteOptional.get();
        }
        throw new BusinessException("No se encontró el reporte con el id: " + id);
    }

    @Override
    public List<Reporte> list() {
        LOGGER.debug("Listado completo de reportes");
        return repository.findAll();
    }

    @Override
    public Page<Reporte> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de reportes - offset: {}, pageSize: {}, pageNumber: {}",
                pageable.getOffset(), pageable.getPageSize(), pageable.getPageNumber());
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<ReporteMapaDTO> obtenerReportesParaMapa() {
        LOGGER.debug("Obteniendo reportes con coordenadas para el mapa");

        List<Reporte> reportes = repository.findByLatIsNotNullAndLngIsNotNull();

        return reportes.stream()
                .map(this::toMapaDTO)
                .toList();
    }

    private ReporteMapaDTO toMapaDTO(Reporte reporte) {
        return ReporteMapaDTO.builder()
                .id(reporte.getId())
                .lat(reporte.getLat())
                .lng(reporte.getLng())
                .ubicacion(reporte.getUbicacion())
                .descripcion(reporte.getDescripcion())
                .foto(reporte.getFoto())
                .estado(reporte.getEstado())
                .tipoReporte(reporte.getTipoReporte() != null ? reporte.getTipoReporte().name() : null)
                .estadoMascota(reporte.getEstadoMascota() != null ? reporte.getEstadoMascota().name() : null)
                .color(reporte.getColorMapa())
                .build();
    }
}