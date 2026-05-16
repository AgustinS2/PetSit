package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefugioRepository extends JpaRepository<Refugio, Long> {

    List<Refugio> findByEstadoAprobacion(String estadoAprobacion);

    long countByEstadoAprobacion(String estadoAprobacion);
}
