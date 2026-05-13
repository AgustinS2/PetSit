package ar.edu.davinci.PetSit.service.Adopcion;

import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdopcionService {

    Adopcion save(final Adopcion adopcion) throws BusinessException;
    Adopcion update(final Adopcion adopcion) throws BusinessException;
    void delete(final Adopcion adopcion);
    void delete(final Long id);
    Adopcion findById(final Long id) throws BusinessException;
    List<Adopcion> list();
    Page<Adopcion> list(Pageable pageable);
    long count();

    /** Solo las adopciones con estado ACTIVA — para el select del formulario de postulación */
    List<Adopcion> listActivas();
}
