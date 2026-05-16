package ar.edu.davinci.PetSit.service.Refugio;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.RefugioRepository;
import ar.edu.davinci.PetSit.service.GeocodificacionService;
import ar.edu.davinci.PetSit.service.GeocodificacionService.Coordenadas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RefugioServiceImpl implements RefugioService {

    private final Logger LOGGER = LoggerFactory.getLogger(RefugioServiceImpl.class);

    @Autowired private RefugioRepository      repository;
    @Autowired private GeocodificacionService geocodificacionService;

    private void geocodificarSiNecesario(Refugio r) {
        if (r.getLat() != null && r.getLng() != null) return;
        String query = buildQuery(r.getDireccion(), r.getUbicacion());
        if (query == null) return;
        Coordenadas coords = geocodificacionService.geocodificar(query);
        if (coords.encontrado()) { r.setLat(coords.lat()); r.setLng(coords.lng()); }
    }

    private String buildQuery(String direccion, String ubicacion) {
        if (direccion != null && !direccion.isBlank()) return direccion;
        if (ubicacion  != null && !ubicacion.isBlank())  return ubicacion;
        return null;
    }

    @Override
    public Refugio save(Refugio r) throws BusinessException {
        if (r.getId() != null) throw new BusinessException("No se puede crear un refugio con ID específico.");
        geocodificarSiNecesario(r);
        return repository.save(r);
    }

    @Override
    public Refugio update(Refugio r) throws BusinessException {
        if (r.getId() == null) throw new BusinessException("No se puede actualizar un refugio sin ID.");
        geocodificarSiNecesario(r);
        return repository.save(r);
    }

    @Override public void delete(Refugio r)  { repository.delete(r); }
    @Override public void delete(Long id)    { repository.deleteById(id); }

    @Override
    public Refugio findById(Long id) throws BusinessException {
        Optional<Refugio> opt = repository.findById(id);
        if (opt.isPresent()) return opt.get();
        throw new BusinessException("Refugio no encontrado: " + id);
    }

    @Override public List<Refugio> list()                  { return repository.findAll(); }
    @Override public Page<Refugio> list(Pageable pageable) { return repository.findAll(pageable); }
    @Override public long count()                          { return repository.count(); }

    @Override
    public List<Refugio> listPendientes() {
        return repository.findByEstadoAprobacion("PENDIENTE");
    }

    @Override
    public List<Refugio> listAprobadas() {
        // Incluye: estadoAprobacion=APROBADA o NULL (filas viejas)
        // Excluye: activa=false explícito (0 en BD)
        return repository.findAll().stream()
                .filter(r -> r.getEstadoAprobacion() == null
                          || "APROBADA".equals(r.getEstadoAprobacion()))
                .filter(r -> !Boolean.FALSE.equals(r.getActiva()))
                .toList();
    }

    @Override
    public long countPendientes() {
        return repository.countByEstadoAprobacion("PENDIENTE");
    }
}
