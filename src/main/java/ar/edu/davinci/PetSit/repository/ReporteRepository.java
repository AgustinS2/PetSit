package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.davinci.PetSit.domain.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
}