package ar.edu.davinci.PetSit.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.domain.TipoUsuario;
import org.springframework.test.context.ActiveProfiles; // PARA QUE LOS TESTS DEJEN DE DEPENDER DE DATA.SQL,SCHEMA.SQL Y SECURITY

@DataJpaTest
@ActiveProfiles("test") // PARA QUE LOS TESTS DEJEN DE DEPENDER DE DATA.SQL,SCHEMA.SQL Y SECURITY
class UsuarioRepositoryTest {
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioRepositoryTest.class);
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void testFindAll() {
		Usuario u = Usuario.builder()
				.nombre("Agustin")
				.apellido("Segovia")
				.correo("ags@gmail.com")
				.tipo(TipoUsuario.DUENO)
				.contrasena("asdasd")
				.telefono("12341234")
				.build();

		usuarioRepository.save(u);

		List<Usuario> usuarios = usuarioRepository.findAll();
		assertFalse(usuarios.isEmpty());
	}


	@Test
	void testFindById() {
		Usuario u = usuarioRepository.save(Usuario.builder()
				.nombre("Matias")
				.apellido("Bernal")
				.correo("matibernal2000@gmail.com")
				.tipo(TipoUsuario.DUENO)
				.contrasena("matute123")
				.telefono("2478556492")
				.build());

		Optional<Usuario> usuarioOptional = usuarioRepository.findById(u.getId());
		assertTrue(usuarioOptional.isPresent());
		assertEquals(TipoUsuario.DUENO, usuarioOptional.get().getTipo());
	}
}