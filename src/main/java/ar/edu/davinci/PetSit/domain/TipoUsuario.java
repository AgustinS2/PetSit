package ar.edu.davinci.PetSit.domain;

import java.util.LinkedList;
import java.util.List;
public enum TipoUsuario {
	
	VETERINARIO("Veterinario"),
	REFUGIO("Refugio"),
	DUENO("Dueno"),
	ADMINISTRADOR("Administrador");
	
private String descripcion;

private TipoUsuario(String descripcion) {
	this.descripcion = descripcion;
}

public String getDescripcion() {
	return descripcion;
}
public static List<TipoUsuario> getTipoUsuarios() {
	List<TipoUsuario> tipoUsuarios = new LinkedList<TipoUsuario>();
	tipoUsuarios.add(TipoUsuario.VETERINARIO);
	tipoUsuarios.add(TipoUsuario.REFUGIO);
	tipoUsuarios.add(TipoUsuario.DUENO);
	
	return tipoUsuarios;
}
}