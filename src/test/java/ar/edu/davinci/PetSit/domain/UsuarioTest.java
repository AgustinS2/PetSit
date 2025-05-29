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
		TipoUsuario tipoUsuario = TipoUsuario.DUENO;
		String contrasena = "asdasd";
		String telefono = "12341234";
		// When
		Usuario usuario = Usuario.builder()
		
		.id(id)
		.nombre(nombre)
		.apellido(apellido)
		.correo(correo)
		.tipo(tipoUsuario)
		.contrasena(contrasena)
		.telefono(telefono)
		.build();
	
		// Then
		assertNotNull(usuario);
		assertEquals(id, usuario.getId());
		assertEquals(nombre, usuario.getNombre());
		assertEquals(apellido, usuario.getApellido());
		assertEquals(tipoUsuario, usuario.getTipo());
		assertEquals(correo, usuario.getCorreo());
		assertEquals(contrasena, usuario.getContrasena());
		assertEquals(telefono, usuario.getTelefono());
		}
}