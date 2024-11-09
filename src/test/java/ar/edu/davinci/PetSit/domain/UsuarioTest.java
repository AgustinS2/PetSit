package ar.edu.davinci.PetSit.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class UsuarioTest {
	@Test
	void testBuilder() {
		// Given
		Long id = 1L;
		String nombre = "Agustin";
		String apellido = "Segovia";
		String correo = "ags@gmail.com";
		TipoUsuario tipoUsuario = TipoUsuario.DUEÑO;
		// When
		Usuario usuario = Usuario.builder()
		
		.id(id)
		.nombre(nombre)
		.apellido(apellido)
		.correo(correo)
		.tipo(tipoUsuario)
		.build();
	
		// Then
		assertNotNull(usuario);
		assertEquals(id, usuario.getId());
		assertEquals(nombre, usuario.getNombre());
		assertEquals(apellido, usuario.getApellido());
		assertEquals(tipoUsuario, usuario.getTipo());
		assertEquals(correo, usuario.getCorreo());
		}
}