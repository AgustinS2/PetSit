package ar.edu.davinci.PetSit.service.Veterinaria;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.PetSit.domain.Veterinaria;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface VeterinariaService {

    Veterinaria save(final Veterinaria veterinaria) throws BusinessException;

    Veterinaria update(final Veterinaria veterinaria) throws BusinessException;

    void delete(final Veterinaria veterinaria);

    void delete(final Long id);

    Veterinaria findById(final Long id) throws BusinessException;

    List<Veterinaria> list();

    Page<Veterinaria> list(Pageable pageable);

    long count();
}
