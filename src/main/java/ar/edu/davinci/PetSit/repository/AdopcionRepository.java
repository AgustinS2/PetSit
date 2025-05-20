package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.davinci.PetSit.domain.Adopcion;

public interface AdopcionRepository extends JpaRepository<Adopcion, Long> {
}
