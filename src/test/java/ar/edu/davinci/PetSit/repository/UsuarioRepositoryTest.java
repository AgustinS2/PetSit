package ar.edu.davinci.PetSit.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.domain.TipoUsuario;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UsuarioRepositoryTest {
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioRepositoryTest.class);
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Test
	void testFindAll() {
	assertNotNull(usuarioRepository, "El repositorio es nulo.");
	List<Usuario> usuarios = usuarioRepository.findAll();
	LOGGER.info("usuarios encontrados: " + usuarios.size());
	assertNotNull(usuarios, "Usuarios es nulo");
	assertTrue(usuarios.size() > 0, "No existen usuarios.");
	
	}
	
	@Test
	void testFindById() {
	Long id = 1L;
	Usuario usuario = null;
	Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
	if (usuarioOptional.isPresent()) {
		usuario = usuarioOptional.get();
		LOGGER.info("Usuario encontrada para el id: " + usuario.getId());
		LOGGER.info("Usuario Tipo: " + usuario.getTipo());
		LOGGER.info("Usuario Tipo.Descripcion: " +
	
	usuario.getTipo().getDescripcion());
	
	assertEquals(TipoUsuario.DUENO, usuario.getTipo());
	} else {
	LOGGER.info("Usuario no encontrada para el id: " + id);
	usuario = usuarioOptional.get();
	assertNull(usuario);
	}
	}
}