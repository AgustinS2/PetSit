package ar.edu.davinci.PetSit.service.Refugio;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.RefugioRepository;

@Service
public class RefugioServiceImpl implements RefugioService {

    private final Logger LOGGER = LoggerFactory.getLogger(RefugioServiceImpl.class);
    private final RefugioRepository repository;

    @Autowired
    public RefugioServiceImpl(final RefugioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Refugio save(final Refugio refugio) throws BusinessException {
        LOGGER.debug("Grabamos el refugio: " + refugio.toString());
        if (refugio.getId() == null) {
            return repository.save(refugio);
        }
        throw new BusinessException("No se puede crear el refugio con un ID específico.");
    }

    @Override
    public Refugio update(final Refugio refugio) throws BusinessException {
        LOGGER.debug("Modificamos el refugio: " + refugio.toString());
        if (refugio.getId() != null) {
            return repository.save(refugio);
        }
        throw new BusinessException("No se puede modificar un refugio que aún no fue creado.");
    }

    @Override
    public void delete(final Refugio refugio) {
        LOGGER.debug("Borramos el refugio: " + refugio.toString());
        repository.delete(refugio);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Borramos el refugio con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Refugio findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos el refugio por ID: " + id);
        Optional<Refugio> refugioOptional = repository.findById(id);
        if (refugioOptional.isPresent()) {
            return refugioOptional.get();
        }
        throw new BusinessException("No se encontró el refugio con el ID: " + id);
    }

    @Override
    public List<Refugio> list() {
        LOGGER.debug("Listado de todos los refugios");
        return repository.findAll();
    }

    @Override
    public Page<Refugio> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de refugios");
        LOGGER.debug("Pageable: offset: " + pageable.getOffset() + ", pageSize: " +
                pageable.getPageSize() + ", pageNumber: " + pageable.getPageNumber());
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
