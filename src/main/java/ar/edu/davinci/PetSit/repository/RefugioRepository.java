package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.davinci.PetSit.domain.Refugio;

public interface RefugioRepository extends JpaRepository<Refugio, Long> {
}
