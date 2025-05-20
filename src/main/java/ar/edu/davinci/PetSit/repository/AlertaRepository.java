package ar.edu.davinci.PetSit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.davinci.PetSit.domain.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}