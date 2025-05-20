package ar.edu.davinci.PetSit.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Alerta;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.AlertaRepository;

@Service
public class AlertaServiceImpl implements AlertaService {

    private final Logger LOGGER = LoggerFactory.getLogger(AlertaServiceImpl.class);
    private final AlertaRepository repository;

    @Autowired
    public AlertaServiceImpl(final AlertaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Alerta save(final Alerta alerta) throws BusinessException {
        LOGGER.debug("Grabamos la alerta: " + alerta.toString());
        if (alerta.getId() == null) {
            return repository.save(alerta);
        }
        throw new BusinessException("No se puede crear la alerta con un ID específico.");
    }

    @Override
    public Alerta update(final Alerta alerta) throws BusinessException {
        LOGGER.debug("Modificamos la alerta: " + alerta.toString());
        if (alerta.getId() != null) {
            return repository.save(alerta);
        }
        throw new BusinessException("No se puede modificar una alerta que aún no fue creada.");
    }

    @Override
    public void delete(final Alerta alerta) {
        LOGGER.debug("Borramos la alerta: " + alerta.toString());
        repository.delete(alerta);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Borramos la alerta con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Alerta findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos la alerta por ID: " + id);
        Optional<Alerta> alertaOptional = repository.findById(id);
        if (alertaOptional.isPresent()) {
            return alertaOptional.get();
        }
        throw new BusinessException("No se encontró la alerta con el ID: " + id);
    }

    @Override
    public List<Alerta> list() {
        LOGGER.debug("Listado de todas las alertas");
        return repository.findAll();
    }

    @Override
    public Page<Alerta> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de alertas");
        LOGGER.debug("Pageable: offset: " + pageable.getOffset() + ", pageSize: " +
                pageable.getPageSize() + ", pageNumber: " + pageable.getPageNumber());
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
