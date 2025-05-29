package ar.edu.davinci.PetSit.service.Adopcion;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.AdopcionRepository;

@Service
public class AdopcionServiceImpl implements AdopcionService {

    private final Logger LOGGER = LoggerFactory.getLogger(AdopcionServiceImpl.class);
    private final AdopcionRepository repository;

    @Autowired
    public AdopcionServiceImpl(final AdopcionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Adopcion save(final Adopcion adopcion) throws BusinessException {
        LOGGER.debug("Guardamos la adopción: " + adopcion.toString());
        if (adopcion.getId() == null) {
            return repository.save(adopcion);
        }
        throw new BusinessException("No se puede crear una adopción con un ID específico.");
    }

    @Override
    public Adopcion update(final Adopcion adopcion) throws BusinessException {
        LOGGER.debug("Actualizamos la adopción: " + adopcion.toString());
        if (adopcion.getId() != null) {
            return repository.save(adopcion);
        }
        throw new BusinessException("No se puede actualizar una adopción que no fue creada.");
    }

    @Override
    public void delete(final Adopcion adopcion) {
        LOGGER.debug("Borramos la adopción: " + adopcion.toString());
        repository.delete(adopcion);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Borramos la adopción con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Adopcion findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos la adopción con ID: " + id);
        Optional<Adopcion> adopcionOptional = repository.findById(id);
        if (adopcionOptional.isPresent()) {
            return adopcionOptional.get();
        }
        throw new BusinessException("No se encontró la adopción con el ID: " + id);
    }

    @Override
    public List<Adopcion> list() {
        LOGGER.debug("Listamos todas las adopciones");
        return repository.findAll();
    }

    @Override
    public Page<Adopcion> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de adopciones");
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
