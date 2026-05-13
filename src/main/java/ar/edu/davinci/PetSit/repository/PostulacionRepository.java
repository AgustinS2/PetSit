package ar.edu.davinci.PetSit.repository;

import ar.edu.davinci.PetSit.domain.Postulacion;
import ar.edu.davinci.PetSit.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    /** Todas las postulaciones de un usuario (para "Mis solicitudes") */
    List<Postulacion> findByUsuario(Usuario usuario);
}