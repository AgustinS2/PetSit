package ar.edu.davinci.PetSit.domain;

import java.util.LinkedList;
import java.util.List;

public enum TipoUsuario {

	VETERINARIO("Veterinario"),
	REFUGIO("Refugio"),
	DUENO("Dueno"),
	ADMINISTRADOR("Administrador");

	private final String descripcion;

	TipoUsuario(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Lista para los <select> del panel admin (incluye ADMINISTRADOR).
	 * El registro público solo usa DUENO, VETERINARIO y REFUGIO — eso lo
	 * maneja el formulario de registro con la clave admin.
	 */
	public static List<TipoUsuario> getTipoUsuarios() {
		List<TipoUsuario> tipos = new LinkedList<>();
		tipos.add(TipoUsuario.ADMINISTRADOR);
		tipos.add(TipoUsuario.DUENO);
		tipos.add(TipoUsuario.VETERINARIO);
		tipos.add(TipoUsuario.REFUGIO);
		return tipos;
	}
}
