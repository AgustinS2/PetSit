package ar.edu.davinci.PetSit.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.PetSit.domain.Adopcion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface AdopcionService {

    Adopcion save(final Adopcion adopcion) throws BusinessException;

    Adopcion update(final Adopcion adopcion) throws BusinessException;

    void delete(final Adopcion adopcion);

    void delete(final Long id);

    Adopcion findById(final Long id) throws BusinessException;

    List<Adopcion> list();

    Page<Adopcion> list(Pageable pageable);

    long count();
}
