package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Reporte;
import ar.edu.davinci.PetSit.domain.TipoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    /** Todos los reportes que tienen coordenadas — para el mapa */
    List<Reporte> findByLatIsNotNullAndLngIsNotNull();

    /** Reportes de un tipo específico con coordenadas */
    List<Reporte> findByTipoReporteAndLatIsNotNullAndLngIsNotNull(TipoReporte tipoReporte);

    /**
     * Reportes dentro de un radio aproximado (en grados, ~0.09° ≈ 10km).
     * Para una distancia exacta usá la fórmula de Haversine en el servicio.
     * Este query hace un bounding-box previo para no traer toda la tabla.
     */
    @Query("""
        SELECT r FROM Reporte r
        WHERE r.lat IS NOT NULL AND r.lng IS NOT NULL
          AND r.lat BETWEEN :latMin AND :latMax
          AND r.lng BETWEEN :lngMin AND :lngMax
    """)
    List<Reporte> findDentroDeRango(
            @Param("latMin") double latMin,
            @Param("latMax") double latMax,
            @Param("lngMin") double lngMin,
            @Param("lngMax") double lngMax
    );
}
