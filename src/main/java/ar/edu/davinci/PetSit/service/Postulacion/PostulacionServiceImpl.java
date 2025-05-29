package ar.edu.davinci.PetSit.service.Postulacion;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.PostulacionRepository;

@Service
public class PostulacionServiceImpl implements PostulacionService {

    private final Logger LOGGER = LoggerFactory.getLogger(PostulacionServiceImpl.class);
    private final PostulacionRepository repository;

    @Autowired
    public PostulacionServiceImpl(final PostulacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Postulacion save(final Postulacion postulacion) throws BusinessException {
        LOGGER.debug("Guardamos la postulación: " + postulacion.toString());
        if (postulacion.getId() == null) {
            return repository.save(postulacion);
        }
        throw new BusinessException("No se puede crear una postulación con un ID específico.");
    }

    @Override
    public Postulacion update(final Postulacion postulacion) throws BusinessException {
        LOGGER.debug("Actualizamos la postulación: " + postulacion.toString());
        if (postulacion.getId() != null) {
            return repository.save(postulacion);
        }
        throw new BusinessException("No se puede actualizar una postulación que no fue creada.");
    }

    @Override
    public void delete(final Postulacion postulacion) {
        LOGGER.debug("Eliminamos la postulación: " + postulacion.toString());
        repository.delete(postulacion);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Eliminamos la postulación con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Postulacion findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos la postulación con ID: " + id);
        Optional<Postulacion> postulacionOptional = repository.findById(id);
        if (postulacionOptional.isPresent()) {
            return postulacionOptional.get();
        }
        throw new BusinessException("No se encontró la postulación con el ID: " + id);
    }

    @Override
    public List<Postulacion> list() {
        LOGGER.debug("Listamos todas las postulaciones");
        return repository.findAll();
    }

    @Override
    public Page<Postulacion> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de postulaciones");
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
