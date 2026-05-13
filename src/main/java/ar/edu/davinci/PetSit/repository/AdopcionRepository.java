package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Adopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdopcionRepository extends JpaRepository<Adopcion, Long> {

    /** Filtra por estado (ACTIVA / PAUSADA / CERRADA) */
    List<Adopcion> findByEstado(String estado);
}