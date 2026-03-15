package ar.edu.davinci.PetSit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.davinci.PetSit.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByCorreo(String correo);
}
