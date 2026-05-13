package ar.edu.davinci.PetSit.service.Adopcion;

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.AdopcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdopcionServiceImpl implements AdopcionService {

    private final Logger LOGGER = LoggerFactory.getLogger(AdopcionServiceImpl.class);

    @Autowired
    private AdopcionRepository repository;

    @Override
    public Adopcion save(Adopcion a) throws BusinessException {
        if (a.getId() == null) return repository.save(a);
        throw new BusinessException("No se puede crear una adopción con ID específico.");
    }

    @Override
    public Adopcion update(Adopcion a) throws BusinessException {
        if (a.getId() != null) return repository.save(a);
        throw new BusinessException("No se puede actualizar una adopción sin ID.");
    }

    @Override public void delete(Adopcion a) { repository.delete(a); }
    @Override public void delete(Long id)    { repository.deleteById(id); }

    @Override
    public Adopcion findById(Long id) throws BusinessException {
        Optional<Adopcion> opt = repository.findById(id);
        if (opt.isPresent()) return opt.get();
        throw new BusinessException("Adopción no encontrada: " + id);
    }

    @Override public List<Adopcion> list()                  { return repository.findAll(); }
    @Override public Page<Adopcion> list(Pageable pageable) { return repository.findAll(pageable); }
    @Override public long count()                           { return repository.count(); }

    /** Solo las activas — para el <select> del formulario de postulación */
    @Override
    public List<Adopcion> listActivas() {
        return repository.findByEstado("ACTIVA");
    }
}
