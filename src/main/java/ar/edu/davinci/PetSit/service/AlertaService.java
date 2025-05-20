package ar.edu.davinci.PetSit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ar.edu.davinci.PetSit.domain.Alerta;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface AlertaService {

    Alerta save(final Alerta alerta) throws BusinessException;

    Alerta update(final Alerta alerta) throws BusinessException;

    void delete(final Alerta alerta);

    void delete(final Long id);

    Alerta findById(final Long id) throws BusinessException;

    List<Alerta> list();

    Page<Alerta> list(Pageable pageable);

    long count();
}
