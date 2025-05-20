package ar.edu.davinci.PetSit.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.VeterinariaRepository;

@Service
public class VeterinariaServiceImpl implements VeterinariaService {

    private final Logger LOGGER = LoggerFactory.getLogger(VeterinariaServiceImpl.class);
    private final VeterinariaRepository repository;

    @Autowired
    public VeterinariaServiceImpl(final VeterinariaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Veterinaria save(final Veterinaria veterinaria) throws BusinessException {
        LOGGER.debug("Guardamos la veterinaria: " + veterinaria.toString());
        if (veterinaria.getId() == null) {
            return repository.save(veterinaria);
        }
        throw new BusinessException("No se puede crear una veterinaria con un ID específico.");
    }

    @Override
    public Veterinaria update(final Veterinaria veterinaria) throws BusinessException {
        LOGGER.debug("Actualizamos la veterinaria: " + veterinaria.toString());
        if (veterinaria.getId() != null) {
            return repository.save(veterinaria);
        }
        throw new BusinessException("No se puede actualizar una veterinaria que no fue creada.");
    }

    @Override
    public void delete(final Veterinaria veterinaria) {
        LOGGER.debug("Eliminamos la veterinaria: " + veterinaria.toString());
        repository.delete(veterinaria);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Eliminamos la veterinaria con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Veterinaria findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos la veterinaria con ID: " + id);
        Optional<Veterinaria> veterinariaOptional = repository.findById(id);
        if (veterinariaOptional.isPresent()) {
            return veterinariaOptional.get();
        }
        throw new BusinessException("No se encontró la veterinaria con el ID: " + id);
    }

    @Override
    public List<Veterinaria> list() {
        LOGGER.debug("Listamos todas las veterinarias");
        return repository.findAll();
    }

    @Override
    public Page<Veterinaria> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de veterinarias");
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
