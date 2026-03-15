package ar.edu.davinci.PetSit.service.Mascota;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.MascotaRepository;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final Logger LOGGER = LoggerFactory.getLogger(MascotaServiceImpl.class);
    private final MascotaRepository repository;

    @Autowired
    public MascotaServiceImpl(final MascotaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mascota save(final Mascota mascota) throws BusinessException {
        LOGGER.debug("Grabamos la mascota: " + mascota.toString());
        if (mascota.getId() == null) {
            return repository.save(mascota);
        }
        throw new BusinessException("No se puede crear la mascota con un ID específico.");
    }

    @Override
    public Mascota update(final Mascota mascota) throws BusinessException {
        LOGGER.debug("Modificamos la mascota: " + mascota.toString());
        if (mascota.getId() != null) {
            return repository.save(mascota);
        }
        throw new BusinessException("No se puede modificar una mascota que aún no fue creada.");
    }

    @Override
    public void delete(final Mascota mascota) {
        LOGGER.debug("Borramos la mascota: " + mascota.toString());
        repository.delete(mascota);
    }

    @Override
    public void delete(final Long id) {
        LOGGER.debug("Borramos la mascota con ID: " + id);
        repository.deleteById(id);
    }

    @Override
    public Mascota findById(final Long id) throws BusinessException {
        LOGGER.debug("Buscamos la mascota por ID: " + id);
        Optional<Mascota> mascotaOptional = repository.findById(id);
        if (mascotaOptional.isPresent()) {
            return mascotaOptional.get();
        }
        throw new BusinessException("No se encontró la mascota con el ID: " + id);
    }

    @Override
    public List<Mascota> list() {
        LOGGER.debug("Listado de todas las mascotas");
        return repository.findAll();
    }

    @Override
    public Page<Mascota> list(Pageable pageable) {
        LOGGER.debug("Listado paginado de mascotas");
        LOGGER.debug("Pageable: offset: " + pageable.getOffset() + ", pageSize: " +
                pageable.getPageSize() + ", pageNumber: " + pageable.getPageNumber());
        return repository.findAll(pageable);
    }

    @Override
    public List<Mascota> findByDueno(Usuario dueno) {
        return repository.findByDueno(dueno);
    }

    @Override
    public long count() {
        return repository.count();
    }
}

