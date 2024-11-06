package PetsitApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import PetsitApp.model.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

}
