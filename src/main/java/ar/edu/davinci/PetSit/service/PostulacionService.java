package ar.edu.davinci.PetSit.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface PostulacionService {

    Postulacion save(final Postulacion postulacion) throws BusinessException;

    Postulacion update(final Postulacion postulacion) throws BusinessException;

    void delete(final Postulacion postulacion);

    void delete(final Long id);

    Postulacion findById(final Long id) throws BusinessException;

    List<Postulacion> list();

    Page<Postulacion> list(Pageable pageable);

    long count();
}
