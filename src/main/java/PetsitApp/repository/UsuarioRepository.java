package PetsitApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import PetsitApp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
