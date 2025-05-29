package ar.edu.davinci.PetSit.service.Refugio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ar.edu.davinci.PetSit.domain.Refugio;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface RefugioService {

    Refugio save(final Refugio refugio) throws BusinessException;

    Refugio update(final Refugio refugio) throws BusinessException;

    void delete(final Refugio refugio);

    void delete(final Long id);

    Refugio findById(final Long id) throws BusinessException;

    List<Refugio> list();

    Page<Refugio> list(Pageable pageable);

    long count();
}

