package ar.edu.davinci.PetSit.service.Veterinaria;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.VeterinariaRepository;
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
public class VeterinariaServiceImpl implements VeterinariaService {

    private final Logger LOGGER = LoggerFactory.getLogger(VeterinariaServiceImpl.class);

    @Autowired private VeterinariaRepository   repository;
    @Autowired private GeocodificacionService  geocodificacionService;

    private void geocodificarSiNecesario(Veterinaria v) {
        if (v.getLat() != null && v.getLng() != null) return; // ya tiene, skip

        // Construir la mejor dirección posible con los datos disponibles
        String query = buildQuery(v.getDireccion(), v.getUbicacion());
        if (query == null) return;

        Coordenadas coords = geocodificacionService.geocodificar(query);
        if (coords.encontrado()) {
            v.setLat(coords.lat());
            v.setLng(coords.lng());
        }
    }

    private String buildQuery(String direccion, String ubicacion) {
        if (direccion != null && !direccion.isBlank()) return direccion;
        if (ubicacion  != null && !ubicacion.isBlank())  return ubicacion;
        return null;
    }

    @Override
    public Veterinaria save(Veterinaria v) throws BusinessException {
        if (v.getId() != null)
            throw new BusinessException("No se puede crear una veterinaria con ID específico.");
        geocodificarSiNecesario(v);
        return repository.save(v);
    }

    @Override
    public Veterinaria update(Veterinaria v) throws BusinessException {
        if (v.getId() == null)
            throw new BusinessException("No se puede actualizar una veterinaria sin ID.");
        geocodificarSiNecesario(v);
        return repository.save(v);
    }

    @Override public void delete(Veterinaria v)  { repository.delete(v); }
    @Override public void delete(Long id)         { repository.deleteById(id); }

    @Override
    public Veterinaria findById(Long id) throws BusinessException {
        Optional<Veterinaria> opt = repository.findById(id);
        if (opt.isPresent()) return opt.get();
        throw new BusinessException("Veterinaria no encontrada: " + id);
    }

    @Override public List<Veterinaria> list()                  { return repository.findAll(); }
    @Override public Page<Veterinaria> list(Pageable pageable) { return repository.findAll(pageable); }
    @Override public long count()                              { return repository.count(); }

    @Override
    public List<Veterinaria> listPendientes() {
        return repository.findByEstadoAprobacion("PENDIENTE");
    }

    @Override
    public List<Veterinaria> listAprobadas() {
        return repository.findByEstadoAprobacion("APROBADA");
    }

    @Override
    public long countPendientes() {
        return repository.countByEstadoAprobacion("PENDIENTE");
    }
}
