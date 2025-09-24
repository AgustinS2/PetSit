package ar.edu.davinci.PetSit.service.Mascota;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ar.edu.davinci.PetSit.domain.Mascota;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface MascotaService {

    Mascota save(final Mascota mascota) throws BusinessException;

    Mascota update(final Mascota mascota) throws BusinessException;

    void delete(final Mascota mascota);

    void delete(final Long id);

    Mascota findById(final Long id) throws BusinessException;

    List<Mascota> list();

    Page<Mascota> list(Pageable pageable);

    long count();
}
