package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.davinci.PetSit.domain.Veterinaria;

public interface VeterinariaRepository extends JpaRepository<Veterinaria, Long> {
}
