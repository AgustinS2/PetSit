package ar.edu.davinci.PetSit.service.Reporte;

import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.dto.ReporteMapaDTO;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReporteService {

    Reporte save(final Reporte reporte) throws BusinessException;
    Reporte update(final Reporte reporte) throws BusinessException;
    void delete(final Reporte reporte);
    void delete(final Long id);
    Reporte findById(final Long id) throws BusinessException;
    List<Reporte> list();
    Page<Reporte> list(Pageable pageable);
    long count();

    /** Todos los reportes con coordenadas, convertidos a DTO para el mapa */
    List<ReporteMapaDTO> obtenerReportesParaMapa();

    /**
     * Guarda el reporte y devuelve los usuarios dentro de ~10km para notificar.
     * La lógica de notificación (email / push) se implementa en el controller.
     */
    List<Usuario> guardarYObtenerUsuariosCercanos(Reporte reporte, List<Usuario> todosUsuarios);
}
