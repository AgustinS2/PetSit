package ar.edu.davinci.PetSit.service.Postulacion;

import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.PostulacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostulacionServiceImpl implements PostulacionService {

    private final Logger LOGGER = LoggerFactory.getLogger(PostulacionServiceImpl.class);
    private final PostulacionRepository repository;

    @Autowired
    public PostulacionServiceImpl(PostulacionRepository repository) {
        this.repository = repository;
    }

    @Override public Postulacion save(Postulacion p) throws BusinessException {
        if (p.getId() == null) return repository.save(p);
        throw new BusinessException("No se puede crear una postulación con ID específico.");
    }

    @Override public Postulacion update(Postulacion p) throws BusinessException {
        if (p.getId() != null) return repository.save(p);
        throw new BusinessException("No se puede actualizar una postulación sin ID.");
    }

    @Override public void delete(Postulacion p)  { repository.delete(p); }
    @Override public void delete(Long id)         { repository.deleteById(id); }

    @Override
    public Postulacion findById(Long id) throws BusinessException {
        Optional<Postulacion> opt = repository.findById(id);
        if (opt.isPresent()) return opt.get();
        throw new BusinessException("Postulación no encontrada: " + id);
    }

    @Override public List<Postulacion> list()                     { return repository.findAll(); }
    @Override public Page<Postulacion> list(Pageable pageable)    { return repository.findAll(pageable); }
    @Override public long count()                                  { return repository.count(); }

    @Override
    public List<Postulacion> findByUsuario(Usuario usuario) {
        return repository.findByUsuario(usuario);
    }
}
