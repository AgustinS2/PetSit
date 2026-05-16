package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Veterinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeterinariaRepository extends JpaRepository<Veterinaria, Long> {

    List<Veterinaria> findByEstadoAprobacion(String estadoAprobacion);

    long countByEstadoAprobacion(String estadoAprobacion);
}
