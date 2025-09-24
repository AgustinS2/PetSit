package ar.edu.davinci.PetSit.service.Reporte;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface ReporteService {

    Reporte save(final Reporte reporte) throws BusinessException;
    Reporte update(final Reporte reporte) throws BusinessException;
    void delete(final Reporte reporte);
    void delete(final Long id);

    Reporte findById(final Long id) throws BusinessException;

    List<Reporte> list();
    Page<Reporte> list(Pageable pageable);

    long count();
}

