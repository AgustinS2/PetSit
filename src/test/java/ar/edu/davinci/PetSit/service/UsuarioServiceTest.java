package ar.edu.davinci.PetSit.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UsuarioServiceTest {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceTest.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	void testList() {
	assertNotNull(usuarioService, "El servico es nulo.");
	List<Usuario> usuarios = usuarioService.list();
	LOGGER.info("Usuarios encontrados: " + usuarios.size());
	assertNotNull(usuarios , "usuarios es nulo");
	assertTrue(usuarios.size() > 0, "No existen usuarios.");
	}
	@Test
	void testTipoUsuario() {
	List<TipoUsuario> list = usuarioService.getTipoUsuarios();
	LOGGER.info("Tipo Usuarios encontrados: " + list.size());
	assertNotNull(list, "tipo usuarios es nulo");
	assertTrue(list.size() == 3, "La cantidad de tipos es distinta");
}
}