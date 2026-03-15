package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.davinci.PetSit.domain.Mascota;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByDuenoUsrId(Long usrId);
}