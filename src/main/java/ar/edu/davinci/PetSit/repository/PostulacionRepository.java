package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.davinci.PetSit.domain.Postulacion;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
}
